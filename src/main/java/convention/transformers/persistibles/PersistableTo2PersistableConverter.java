/**
 * 07/06/2011 13:02:30 Copyright (C) 2011 10Pines S.R.L.
 */
package convention.transformers.persistibles;

import ar.com.kfgodel.appbyconvention.tos.PersistableTo;
import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.orm.api.HibernateOrm;
import ar.com.kfgodel.orm.api.entities.Persistable;
import ar.com.kfgodel.orm.api.operations.basic.FindById;
import net.sf.kfgodel.bean2bean.Bean2Bean;
import net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;
import net.sf.kfgodel.dgarcia.lang.reflection.ReflectionUtils;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Esta clase sabe como convertir de un TO a un objeto de dominio usando su ID para buscarlo en la
 * base
 *
 * @author D. García
 */
public class PersistableTo2PersistableConverter implements SpecializedTypeConverter<PersistableTo, Persistable> {

  @Inject
  private HibernateOrm hibernate;

  @Inject
  private Bean2Bean bean2Bean;

  /**
   * @see SpecializedTypeConverter#convertTo(Type,
   * Object, Annotation[])
   */
  @Override
  public Persistable convertTo(final Type expectedType, final PersistableTo sourceObject,
                               final Annotation[] contextAnnotations) throws CannotConvertException {
    if (sourceObject == null) {
      return null;
    }
    final Class<Persistable> persistableClass = ensureCorrectDestinationType(expectedType, sourceObject);

    Persistable persistable =
      Nary.ofNullable(sourceObject.getId()) //El id puede ser nulo, si no estaba previamente persistido
        // Si el id no es nulo, intentamos buscarlo en la base
        .flatMapNary((persistableId) -> hibernate.ensureSessionFor(FindById.create(persistableClass, persistableId)))
        // Si no tenía id, o si no lo encontramos en la base, en cualquier caso creamos una nueva instancia
        .orElseGet(() -> ReflectionUtils.createInstance(persistableClass));

    // Ya sea una instancia nueva o existente, le copiamos los atributos del TO
    bean2Bean.copyPropertiesTo(persistable, sourceObject);
    return persistable;
  }

  /**
   * Generates user friendly errors if the expected type is not a concrete subtype of Persistable
   */
  private Class<Persistable> ensureCorrectDestinationType(Type expectedType, PersistableTo sourceObject) {
    Class<?> persistableType;
    try {
      persistableType = (Class<?>) expectedType;
    } catch (final ClassCastException e) {
      throw new CannotConvertException("El tipo esperado no es una clase: " + expectedType, sourceObject, expectedType, e);
    }
    if (!Persistable.class.isAssignableFrom(persistableType)) {
      throw new CannotConvertException("El tipo esperado no es un sub tipo de " + Persistable.class + "no podemos convertirlo",
        sourceObject, expectedType);
    }
    return (Class<Persistable>) persistableType;
  }

  public static PersistableTo2PersistableConverter create(Bean2Bean b2b, HibernateOrm hibernate) {
    PersistableTo2PersistableConverter converter = new PersistableTo2PersistableConverter();
    converter.bean2Bean = b2b;
    converter.hibernate = hibernate;
    return converter;
  }

}
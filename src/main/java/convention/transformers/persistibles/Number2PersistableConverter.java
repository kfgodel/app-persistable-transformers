package convention.transformers.persistibles;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.tenpines.orm.api.HibernateOrm;
import ar.com.tenpines.orm.api.entities.Persistable;
import ar.com.tenpines.orm.api.operations.basic.FindById;
import net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * This type knows how to fetch a persisten object given its id
 * Created by kfgodel on 05/04/15.
 */
public class Number2PersistableConverter implements SpecializedTypeConverter<Number, Persistable> {

  @Inject
  private HibernateOrm hibernate;

  @Override
  public Persistable convertTo(Type expectedType, Number sourceObject, Annotation[] contextAnnotations) throws CannotConvertException {
    if (sourceObject == null) {
      return null;
    }
    Class<? extends Persistable> expectedClass = ensureCorrectDestinationType(expectedType, sourceObject);

    Nary<? extends Persistable> foundObject = this.hibernate.ensureSessionFor(FindById.create(expectedClass, sourceObject.longValue()));
    // Si no lo encontramos será null
    return foundObject
      .orElse(null);
  }

  /**
   * Checks and generates user friendly errors if the expected type is not a Persistable concrete sub type
   */
  private Class<? extends Persistable> ensureCorrectDestinationType(Type expectedType, Number sourceObject) {
    if (!Class.class.isInstance(expectedType)) {
      throw new CannotConvertException("El tipo indicado como esperado no es una clase", sourceObject, expectedType);
    }

    if (!Persistable.class.isAssignableFrom((Class) expectedType)) {
      throw new CannotConvertException("La clase pasada no hereda de " + Persistable.class, sourceObject, expectedType);
    }

    return (Class<? extends Persistable>) expectedType;
  }

  public static Number2PersistableConverter create(HibernateOrm hibernate) {
    Number2PersistableConverter converter = new Number2PersistableConverter();
    converter.hibernate = hibernate;
    return converter;
  }

}

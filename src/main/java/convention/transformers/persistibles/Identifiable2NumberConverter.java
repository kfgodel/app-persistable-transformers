package convention.transformers.persistibles;

import ar.com.tenpines.orm.api.entities.Identifiable;
import net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * This type allows conversion from any identifiable to its ID representation
 * Created by kfgodel on 05/04/15.
 */
public class Identifiable2NumberConverter implements SpecializedTypeConverter<Identifiable, Number> {

  @Override
  public Number convertTo(Type expectedType, Identifiable sourceObject, Annotation[] contextAnnotations) throws CannotConvertException {
    if (sourceObject == null) {
      return null;
    }
    return sourceObject.getId();
  }

  public static Identifiable2NumberConverter create() {
    Identifiable2NumberConverter converter = new Identifiable2NumberConverter();
    return converter;
  }

}

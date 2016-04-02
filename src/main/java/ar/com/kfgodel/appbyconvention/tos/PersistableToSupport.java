/**
 * 07/06/2011 12:51:12 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.com.kfgodel.appbyconvention.tos;

import ar.com.tenpines.orm.api.entities.Persistable;
import net.sf.kfgodel.bean2bean.annotations.CopyFrom;

/**
 * Esta clase es una base para la implementación de TOs basados en entidades de persistables.<br>
 * Cuando se genera un TO a partir de una entidad se toma el ID de la entidad como parte de los
 * atributos. Al generar la entidad desde el TO, se utiliza el ID del TO para buscar la entidad en
 * la base. Si no existe o no tiene ID se creará una.
 *
 * @author D. García
 */
public class PersistableToSupport implements PersistableTo {

  @CopyFrom(Persistable.id_FIELD)
  private Long id;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(final Long id) {
    this.id = id;
  }

}
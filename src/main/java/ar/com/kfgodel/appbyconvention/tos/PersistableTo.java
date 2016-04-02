/**
 * 07/06/2011 12:44:03 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.com.kfgodel.appbyconvention.tos;


/**
 * Esta interfaz define el comportamiento esperado para todas aquellas clases de TOs que representan a una entidad persistible
 * identificable por ID.<br>
 * A través de instancias de este tipo podemos enviar mensajes a otro sistema que referencian entidades persistentes
 * sin mover todo el grafo de objetos, y también recibir mensajes referentes a entidades persistentes para afectarlas.<br>
 * <br>
 * Se puede ver a este tipo como una "vista" de los datos proyectados y simplificados de un {@link ar.com.tenpines.orm.api.entities.Persistable}
 * adaptada esa vista a una necesidad de comunicación con un sistema externo (ya sea de entrada o salida).
 *
 * @author D. García
 */
public interface PersistableTo {

  /**
   * Devuelve el ID que identifica al persistable al que hacemos referencia desde este TO
   *
   * @return El id del persistiable o null si no tiene id (tiene null)
   */
  public Long getId();

  /**
   * Establece el ID de la entidad referenciada
   *
   * @param id El Id que representa a la entidad
   */
  public void setId(Long id);
}

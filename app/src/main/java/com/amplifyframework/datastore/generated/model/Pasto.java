package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Pasto type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Pastos", authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "byDieta", fields = {"dietaID"})
public final class Pasto implements Model {
  public static final QueryField ID = field("Pasto", "id");
  public static final QueryField NOME = field("Pasto", "nome");
  public static final QueryField QUANTITA_CROCCANTINI = field("Pasto", "quantitaCroccantini");
  public static final QueryField QUANTITA_UMIDO = field("Pasto", "quantitaUmido");
  public static final QueryField NOTE = field("Pasto", "note");
  public static final QueryField DATA = field("Pasto", "data");
  public static final QueryField ORA = field("Pasto", "ora");
  public static final QueryField DIETA_ID = field("Pasto", "dietaID");
  public static final QueryField ID_GOOGLE_UTENTE = field("Pasto", "idGoogleUtente");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String") String nome;
  private final @ModelField(targetType="Int") Integer quantitaCroccantini;
  private final @ModelField(targetType="Int") Integer quantitaUmido;
  private final @ModelField(targetType="String") String note;
  private final @ModelField(targetType="String") String data;
  private final @ModelField(targetType="String") String ora;
  private final @ModelField(targetType="ID") String dietaID;
  private final @ModelField(targetType="String", isRequired = true) String idGoogleUtente;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getNome() {
      return nome;
  }
  
  public Integer getQuantitaCroccantini() {
      return quantitaCroccantini;
  }
  
  public Integer getQuantitaUmido() {
      return quantitaUmido;
  }
  
  public String getNote() {
      return note;
  }
  
  public String getData() {
      return data;
  }
  
  public String getOra() {
      return ora;
  }
  
  public String getDietaId() {
      return dietaID;
  }
  
  public String getIdGoogleUtente() {
      return idGoogleUtente;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Pasto(String id, String nome, Integer quantitaCroccantini, Integer quantitaUmido, String note, String data, String ora, String dietaID, String idGoogleUtente) {
    this.id = id;
    this.nome = nome;
    this.quantitaCroccantini = quantitaCroccantini;
    this.quantitaUmido = quantitaUmido;
    this.note = note;
    this.data = data;
    this.ora = ora;
    this.dietaID = dietaID;
    this.idGoogleUtente = idGoogleUtente;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Pasto pasto = (Pasto) obj;
      return ObjectsCompat.equals(getId(), pasto.getId()) &&
              ObjectsCompat.equals(getNome(), pasto.getNome()) &&
              ObjectsCompat.equals(getQuantitaCroccantini(), pasto.getQuantitaCroccantini()) &&
              ObjectsCompat.equals(getQuantitaUmido(), pasto.getQuantitaUmido()) &&
              ObjectsCompat.equals(getNote(), pasto.getNote()) &&
              ObjectsCompat.equals(getData(), pasto.getData()) &&
              ObjectsCompat.equals(getOra(), pasto.getOra()) &&
              ObjectsCompat.equals(getDietaId(), pasto.getDietaId()) &&
              ObjectsCompat.equals(getIdGoogleUtente(), pasto.getIdGoogleUtente()) &&
              ObjectsCompat.equals(getCreatedAt(), pasto.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), pasto.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getNome())
      .append(getQuantitaCroccantini())
      .append(getQuantitaUmido())
      .append(getNote())
      .append(getData())
      .append(getOra())
      .append(getDietaId())
      .append(getIdGoogleUtente())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Pasto {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("nome=" + String.valueOf(getNome()) + ", ")
      .append("quantitaCroccantini=" + String.valueOf(getQuantitaCroccantini()) + ", ")
      .append("quantitaUmido=" + String.valueOf(getQuantitaUmido()) + ", ")
      .append("note=" + String.valueOf(getNote()) + ", ")
      .append("data=" + String.valueOf(getData()) + ", ")
      .append("ora=" + String.valueOf(getOra()) + ", ")
      .append("dietaID=" + String.valueOf(getDietaId()) + ", ")
      .append("idGoogleUtente=" + String.valueOf(getIdGoogleUtente()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static IdGoogleUtenteStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   * @throws IllegalArgumentException Checks that ID is in the proper format
   */
  public static Pasto justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new Pasto(
      id,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      nome,
      quantitaCroccantini,
      quantitaUmido,
      note,
      data,
      ora,
      dietaID,
      idGoogleUtente);
  }
  public interface IdGoogleUtenteStep {
    BuildStep idGoogleUtente(String idGoogleUtente);
  }
  

  public interface BuildStep {
    Pasto build();
    BuildStep id(String id) throws IllegalArgumentException;
    BuildStep nome(String nome);
    BuildStep quantitaCroccantini(Integer quantitaCroccantini);
    BuildStep quantitaUmido(Integer quantitaUmido);
    BuildStep note(String note);
    BuildStep data(String data);
    BuildStep ora(String ora);
    BuildStep dietaId(String dietaId);
  }
  

  public static class Builder implements IdGoogleUtenteStep, BuildStep {
    private String id;
    private String idGoogleUtente;
    private String nome;
    private Integer quantitaCroccantini;
    private Integer quantitaUmido;
    private String note;
    private String data;
    private String ora;
    private String dietaID;
    @Override
     public Pasto build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Pasto(
          id,
          nome,
          quantitaCroccantini,
          quantitaUmido,
          note,
          data,
          ora,
          dietaID,
          idGoogleUtente);
    }
    
    @Override
     public BuildStep idGoogleUtente(String idGoogleUtente) {
        Objects.requireNonNull(idGoogleUtente);
        this.idGoogleUtente = idGoogleUtente;
        return this;
    }
    
    @Override
     public BuildStep nome(String nome) {
        this.nome = nome;
        return this;
    }
    
    @Override
     public BuildStep quantitaCroccantini(Integer quantitaCroccantini) {
        this.quantitaCroccantini = quantitaCroccantini;
        return this;
    }
    
    @Override
     public BuildStep quantitaUmido(Integer quantitaUmido) {
        this.quantitaUmido = quantitaUmido;
        return this;
    }
    
    @Override
     public BuildStep note(String note) {
        this.note = note;
        return this;
    }
    
    @Override
     public BuildStep data(String data) {
        this.data = data;
        return this;
    }
    
    @Override
     public BuildStep ora(String ora) {
        this.ora = ora;
        return this;
    }
    
    @Override
     public BuildStep dietaId(String dietaId) {
        this.dietaID = dietaId;
        return this;
    }
    
    /** 
     * WARNING: Do not set ID when creating a new object. Leave this blank and one will be auto generated for you.
     * This should only be set when referring to an already existing object.
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     * @throws IllegalArgumentException Checks that ID is in the proper format
     */
    public BuildStep id(String id) throws IllegalArgumentException {
        this.id = id;
        
        try {
            UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
        } catch (Exception exception) {
          throw new IllegalArgumentException("Model IDs must be unique in the format of UUID.",
                    exception);
        }
        
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String nome, Integer quantitaCroccantini, Integer quantitaUmido, String note, String data, String ora, String dietaId, String idGoogleUtente) {
      super.id(id);
      super.idGoogleUtente(idGoogleUtente)
        .nome(nome)
        .quantitaCroccantini(quantitaCroccantini)
        .quantitaUmido(quantitaUmido)
        .note(note)
        .data(data)
        .ora(ora)
        .dietaId(dietaId);
    }
    
    @Override
     public CopyOfBuilder idGoogleUtente(String idGoogleUtente) {
      return (CopyOfBuilder) super.idGoogleUtente(idGoogleUtente);
    }
    
    @Override
     public CopyOfBuilder nome(String nome) {
      return (CopyOfBuilder) super.nome(nome);
    }
    
    @Override
     public CopyOfBuilder quantitaCroccantini(Integer quantitaCroccantini) {
      return (CopyOfBuilder) super.quantitaCroccantini(quantitaCroccantini);
    }
    
    @Override
     public CopyOfBuilder quantitaUmido(Integer quantitaUmido) {
      return (CopyOfBuilder) super.quantitaUmido(quantitaUmido);
    }
    
    @Override
     public CopyOfBuilder note(String note) {
      return (CopyOfBuilder) super.note(note);
    }
    
    @Override
     public CopyOfBuilder data(String data) {
      return (CopyOfBuilder) super.data(data);
    }
    
    @Override
     public CopyOfBuilder ora(String ora) {
      return (CopyOfBuilder) super.ora(ora);
    }
    
    @Override
     public CopyOfBuilder dietaId(String dietaId) {
      return (CopyOfBuilder) super.dietaId(dietaId);
    }
  }
  
}

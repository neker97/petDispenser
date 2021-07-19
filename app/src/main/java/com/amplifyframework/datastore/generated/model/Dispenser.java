package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.HasMany;
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

/** This is an auto generated class representing the Dispenser type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Dispensers", authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class Dispenser implements Model {
  public static final QueryField ID = field("Dispenser", "id");
  public static final QueryField NOME = field("Dispenser", "nome");
  public static final QueryField CODICE_BLUETOOTH = field("Dispenser", "codiceBluetooth");
  public static final QueryField ID_GOOGLE_UTENTE = field("Dispenser", "idGoogleUtente");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String nome;
  private final @ModelField(targetType="String") String codiceBluetooth;
  private final @ModelField(targetType="Dieta") @HasMany(associatedWith = "dispenserID", type = Dieta.class) List<Dieta> DieteDispenser = null;
  private final @ModelField(targetType="String", isRequired = true) String idGoogleUtente;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getNome() {
      return nome;
  }
  
  public String getCodiceBluetooth() {
      return codiceBluetooth;
  }
  
  public List<Dieta> getDieteDispenser() {
      return DieteDispenser;
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
  
  private Dispenser(String id, String nome, String codiceBluetooth, String idGoogleUtente) {
    this.id = id;
    this.nome = nome;
    this.codiceBluetooth = codiceBluetooth;
    this.idGoogleUtente = idGoogleUtente;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Dispenser dispenser = (Dispenser) obj;
      return ObjectsCompat.equals(getId(), dispenser.getId()) &&
              ObjectsCompat.equals(getNome(), dispenser.getNome()) &&
              ObjectsCompat.equals(getCodiceBluetooth(), dispenser.getCodiceBluetooth()) &&
              ObjectsCompat.equals(getIdGoogleUtente(), dispenser.getIdGoogleUtente()) &&
              ObjectsCompat.equals(getCreatedAt(), dispenser.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), dispenser.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getNome())
      .append(getCodiceBluetooth())
      .append(getIdGoogleUtente())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Dispenser {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("nome=" + String.valueOf(getNome()) + ", ")
      .append("codiceBluetooth=" + String.valueOf(getCodiceBluetooth()) + ", ")
      .append("idGoogleUtente=" + String.valueOf(getIdGoogleUtente()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static NomeStep builder() {
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
  public static Dispenser justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new Dispenser(
      id,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      nome,
      codiceBluetooth,
      idGoogleUtente);
  }
  public interface NomeStep {
    IdGoogleUtenteStep nome(String nome);
  }
  

  public interface IdGoogleUtenteStep {
    BuildStep idGoogleUtente(String idGoogleUtente);
  }
  

  public interface BuildStep {
    Dispenser build();
    BuildStep id(String id) throws IllegalArgumentException;
    BuildStep codiceBluetooth(String codiceBluetooth);
  }
  

  public static class Builder implements NomeStep, IdGoogleUtenteStep, BuildStep {
    private String id;
    private String nome;
    private String idGoogleUtente;
    private String codiceBluetooth;
    @Override
     public Dispenser build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Dispenser(
          id,
          nome,
          codiceBluetooth,
          idGoogleUtente);
    }
    
    @Override
     public IdGoogleUtenteStep nome(String nome) {
        Objects.requireNonNull(nome);
        this.nome = nome;
        return this;
    }
    
    @Override
     public BuildStep idGoogleUtente(String idGoogleUtente) {
        Objects.requireNonNull(idGoogleUtente);
        this.idGoogleUtente = idGoogleUtente;
        return this;
    }
    
    @Override
     public BuildStep codiceBluetooth(String codiceBluetooth) {
        this.codiceBluetooth = codiceBluetooth;
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
    private CopyOfBuilder(String id, String nome, String codiceBluetooth, String idGoogleUtente) {
      super.id(id);
      super.nome(nome)
        .idGoogleUtente(idGoogleUtente)
        .codiceBluetooth(codiceBluetooth);
    }
    
    @Override
     public CopyOfBuilder nome(String nome) {
      return (CopyOfBuilder) super.nome(nome);
    }
    
    @Override
     public CopyOfBuilder idGoogleUtente(String idGoogleUtente) {
      return (CopyOfBuilder) super.idGoogleUtente(idGoogleUtente);
    }
    
    @Override
     public CopyOfBuilder codiceBluetooth(String codiceBluetooth) {
      return (CopyOfBuilder) super.codiceBluetooth(codiceBluetooth);
    }
  }
  
}

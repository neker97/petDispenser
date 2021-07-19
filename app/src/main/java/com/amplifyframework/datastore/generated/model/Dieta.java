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

/** This is an auto generated class representing the Dieta type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Dietas", authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "byDispenser", fields = {"dispenserID"})
@Index(name = "byAnimale", fields = {"animaleID"})
public final class Dieta implements Model {
  public static final QueryField ID = field("Dieta", "id");
  public static final QueryField NOME_DIETA = field("Dieta", "nomeDieta");
  public static final QueryField NOTE = field("Dieta", "note");
  public static final QueryField DIETA_ATTIVA = field("Dieta", "dietaAttiva");
  public static final QueryField DISPENSER_ID = field("Dieta", "dispenserID");
  public static final QueryField ANIMALE_ID = field("Dieta", "animaleID");
  public static final QueryField ID_GOOGLE_UTENTE = field("Dieta", "idGoogleUtente");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String nomeDieta;
  private final @ModelField(targetType="String") String note;
  private final @ModelField(targetType="String") String dietaAttiva;
  private final @ModelField(targetType="ID") String dispenserID;
  private final @ModelField(targetType="ID") String animaleID;
  private final @ModelField(targetType="Pasto") @HasMany(associatedWith = "dietaID", type = Pasto.class) List<Pasto> pastoDietaId = null;
  private final @ModelField(targetType="String", isRequired = true) String idGoogleUtente;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getNomeDieta() {
      return nomeDieta;
  }
  
  public String getNote() {
      return note;
  }
  
  public String getDietaAttiva() {
      return dietaAttiva;
  }
  
  public String getDispenserId() {
      return dispenserID;
  }
  
  public String getAnimaleId() {
      return animaleID;
  }
  
  public List<Pasto> getPastoDietaId() {
      return pastoDietaId;
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
  
  private Dieta(String id, String nomeDieta, String note, String dietaAttiva, String dispenserID, String animaleID, String idGoogleUtente) {
    this.id = id;
    this.nomeDieta = nomeDieta;
    this.note = note;
    this.dietaAttiva = dietaAttiva;
    this.dispenserID = dispenserID;
    this.animaleID = animaleID;
    this.idGoogleUtente = idGoogleUtente;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Dieta dieta = (Dieta) obj;
      return ObjectsCompat.equals(getId(), dieta.getId()) &&
              ObjectsCompat.equals(getNomeDieta(), dieta.getNomeDieta()) &&
              ObjectsCompat.equals(getNote(), dieta.getNote()) &&
              ObjectsCompat.equals(getDietaAttiva(), dieta.getDietaAttiva()) &&
              ObjectsCompat.equals(getDispenserId(), dieta.getDispenserId()) &&
              ObjectsCompat.equals(getAnimaleId(), dieta.getAnimaleId()) &&
              ObjectsCompat.equals(getIdGoogleUtente(), dieta.getIdGoogleUtente()) &&
              ObjectsCompat.equals(getCreatedAt(), dieta.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), dieta.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getNomeDieta())
      .append(getNote())
      .append(getDietaAttiva())
      .append(getDispenserId())
      .append(getAnimaleId())
      .append(getIdGoogleUtente())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Dieta {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("nomeDieta=" + String.valueOf(getNomeDieta()) + ", ")
      .append("note=" + String.valueOf(getNote()) + ", ")
      .append("dietaAttiva=" + String.valueOf(getDietaAttiva()) + ", ")
      .append("dispenserID=" + String.valueOf(getDispenserId()) + ", ")
      .append("animaleID=" + String.valueOf(getAnimaleId()) + ", ")
      .append("idGoogleUtente=" + String.valueOf(getIdGoogleUtente()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static NomeDietaStep builder() {
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
  public static Dieta justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new Dieta(
      id,
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
      nomeDieta,
      note,
      dietaAttiva,
      dispenserID,
      animaleID,
      idGoogleUtente);
  }
  public interface NomeDietaStep {
    IdGoogleUtenteStep nomeDieta(String nomeDieta);
  }
  

  public interface IdGoogleUtenteStep {
    BuildStep idGoogleUtente(String idGoogleUtente);
  }
  

  public interface BuildStep {
    Dieta build();
    BuildStep id(String id) throws IllegalArgumentException;
    BuildStep note(String note);
    BuildStep dietaAttiva(String dietaAttiva);
    BuildStep dispenserId(String dispenserId);
    BuildStep animaleId(String animaleId);
  }
  

  public static class Builder implements NomeDietaStep, IdGoogleUtenteStep, BuildStep {
    private String id;
    private String nomeDieta;
    private String idGoogleUtente;
    private String note;
    private String dietaAttiva;
    private String dispenserID;
    private String animaleID;
    @Override
     public Dieta build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Dieta(
          id,
          nomeDieta,
          note,
          dietaAttiva,
          dispenserID,
          animaleID,
          idGoogleUtente);
    }
    
    @Override
     public IdGoogleUtenteStep nomeDieta(String nomeDieta) {
        Objects.requireNonNull(nomeDieta);
        this.nomeDieta = nomeDieta;
        return this;
    }
    
    @Override
     public BuildStep idGoogleUtente(String idGoogleUtente) {
        Objects.requireNonNull(idGoogleUtente);
        this.idGoogleUtente = idGoogleUtente;
        return this;
    }
    
    @Override
     public BuildStep note(String note) {
        this.note = note;
        return this;
    }
    
    @Override
     public BuildStep dietaAttiva(String dietaAttiva) {
        this.dietaAttiva = dietaAttiva;
        return this;
    }
    
    @Override
     public BuildStep dispenserId(String dispenserId) {
        this.dispenserID = dispenserId;
        return this;
    }
    
    @Override
     public BuildStep animaleId(String animaleId) {
        this.animaleID = animaleId;
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
    private CopyOfBuilder(String id, String nomeDieta, String note, String dietaAttiva, String dispenserId, String animaleId, String idGoogleUtente) {
      super.id(id);
      super.nomeDieta(nomeDieta)
        .idGoogleUtente(idGoogleUtente)
        .note(note)
        .dietaAttiva(dietaAttiva)
        .dispenserId(dispenserId)
        .animaleId(animaleId);
    }
    
    @Override
     public CopyOfBuilder nomeDieta(String nomeDieta) {
      return (CopyOfBuilder) super.nomeDieta(nomeDieta);
    }
    
    @Override
     public CopyOfBuilder idGoogleUtente(String idGoogleUtente) {
      return (CopyOfBuilder) super.idGoogleUtente(idGoogleUtente);
    }
    
    @Override
     public CopyOfBuilder note(String note) {
      return (CopyOfBuilder) super.note(note);
    }
    
    @Override
     public CopyOfBuilder dietaAttiva(String dietaAttiva) {
      return (CopyOfBuilder) super.dietaAttiva(dietaAttiva);
    }
    
    @Override
     public CopyOfBuilder dispenserId(String dispenserId) {
      return (CopyOfBuilder) super.dispenserId(dispenserId);
    }
    
    @Override
     public CopyOfBuilder animaleId(String animaleId) {
      return (CopyOfBuilder) super.animaleId(animaleId);
    }
  }
  
}

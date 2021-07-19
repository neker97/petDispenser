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

/** This is an auto generated class representing the Animale type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Animales", authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class Animale implements Model {
  public static final QueryField ID = field("Animale", "id");
  public static final QueryField NOME_ANIMALE = field("Animale", "nomeAnimale");
  public static final QueryField ID_GOOGLE_UTENTE = field("Animale", "idGoogleUtente");
  public static final QueryField TIPOLOGIA = field("Animale", "tipologia");
  public static final QueryField RAZZA = field("Animale", "razza");
  public static final QueryField PESO = field("Animale", "peso");
  public static final QueryField DDN = field("Animale", "ddn");
  public static final QueryField PATH = field("Animale", "path");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String nomeAnimale;
  private final @ModelField(targetType="String", isRequired = true) String idGoogleUtente;
  private final @ModelField(targetType="String") String tipologia;
  private final @ModelField(targetType="String") String razza;
  private final @ModelField(targetType="Float") Double peso;
  private final @ModelField(targetType="String") String ddn;
  private final @ModelField(targetType="String") String path;
  private final @ModelField(targetType="Dieta") @HasMany(associatedWith = "animaleID", type = Dieta.class) List<Dieta> dietaAnimaleId = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getNomeAnimale() {
      return nomeAnimale;
  }
  
  public String getIdGoogleUtente() {
      return idGoogleUtente;
  }
  
  public String getTipologia() {
      return tipologia;
  }
  
  public String getRazza() {
      return razza;
  }
  
  public Double getPeso() {
      return peso;
  }
  
  public String getDdn() {
      return ddn;
  }
  
  public String getPath() {
      return path;
  }
  
  public List<Dieta> getDietaAnimaleId() {
      return dietaAnimaleId;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Animale(String id, String nomeAnimale, String idGoogleUtente, String tipologia, String razza, Double peso, String ddn, String path) {
    this.id = id;
    this.nomeAnimale = nomeAnimale;
    this.idGoogleUtente = idGoogleUtente;
    this.tipologia = tipologia;
    this.razza = razza;
    this.peso = peso;
    this.ddn = ddn;
    this.path = path;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Animale animale = (Animale) obj;
      return ObjectsCompat.equals(getId(), animale.getId()) &&
              ObjectsCompat.equals(getNomeAnimale(), animale.getNomeAnimale()) &&
              ObjectsCompat.equals(getIdGoogleUtente(), animale.getIdGoogleUtente()) &&
              ObjectsCompat.equals(getTipologia(), animale.getTipologia()) &&
              ObjectsCompat.equals(getRazza(), animale.getRazza()) &&
              ObjectsCompat.equals(getPeso(), animale.getPeso()) &&
              ObjectsCompat.equals(getDdn(), animale.getDdn()) &&
              ObjectsCompat.equals(getPath(), animale.getPath()) &&
              ObjectsCompat.equals(getCreatedAt(), animale.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), animale.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getNomeAnimale())
      .append(getIdGoogleUtente())
      .append(getTipologia())
      .append(getRazza())
      .append(getPeso())
      .append(getDdn())
      .append(getPath())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Animale {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("nomeAnimale=" + String.valueOf(getNomeAnimale()) + ", ")
      .append("idGoogleUtente=" + String.valueOf(getIdGoogleUtente()) + ", ")
      .append("tipologia=" + String.valueOf(getTipologia()) + ", ")
      .append("razza=" + String.valueOf(getRazza()) + ", ")
      .append("peso=" + String.valueOf(getPeso()) + ", ")
      .append("ddn=" + String.valueOf(getDdn()) + ", ")
      .append("path=" + String.valueOf(getPath()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static NomeAnimaleStep builder() {
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
  public static Animale justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new Animale(
      id,
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
      nomeAnimale,
      idGoogleUtente,
      tipologia,
      razza,
      peso,
      ddn,
      path);
  }
  public interface NomeAnimaleStep {
    IdGoogleUtenteStep nomeAnimale(String nomeAnimale);
  }
  

  public interface IdGoogleUtenteStep {
    BuildStep idGoogleUtente(String idGoogleUtente);
  }
  

  public interface BuildStep {
    Animale build();
    BuildStep id(String id) throws IllegalArgumentException;
    BuildStep tipologia(String tipologia);
    BuildStep razza(String razza);
    BuildStep peso(Double peso);
    BuildStep ddn(String ddn);
    BuildStep path(String path);
  }
  

  public static class Builder implements NomeAnimaleStep, IdGoogleUtenteStep, BuildStep {
    private String id;
    private String nomeAnimale;
    private String idGoogleUtente;
    private String tipologia;
    private String razza;
    private Double peso;
    private String ddn;
    private String path;
    @Override
     public Animale build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Animale(
          id,
          nomeAnimale,
          idGoogleUtente,
          tipologia,
          razza,
          peso,
          ddn,
          path);
    }
    
    @Override
     public IdGoogleUtenteStep nomeAnimale(String nomeAnimale) {
        Objects.requireNonNull(nomeAnimale);
        this.nomeAnimale = nomeAnimale;
        return this;
    }
    
    @Override
     public BuildStep idGoogleUtente(String idGoogleUtente) {
        Objects.requireNonNull(idGoogleUtente);
        this.idGoogleUtente = idGoogleUtente;
        return this;
    }
    
    @Override
     public BuildStep tipologia(String tipologia) {
        this.tipologia = tipologia;
        return this;
    }
    
    @Override
     public BuildStep razza(String razza) {
        this.razza = razza;
        return this;
    }
    
    @Override
     public BuildStep peso(Double peso) {
        this.peso = peso;
        return this;
    }
    
    @Override
     public BuildStep ddn(String ddn) {
        this.ddn = ddn;
        return this;
    }
    
    @Override
     public BuildStep path(String path) {
        this.path = path;
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
    private CopyOfBuilder(String id, String nomeAnimale, String idGoogleUtente, String tipologia, String razza, Double peso, String ddn, String path) {
      super.id(id);
      super.nomeAnimale(nomeAnimale)
        .idGoogleUtente(idGoogleUtente)
        .tipologia(tipologia)
        .razza(razza)
        .peso(peso)
        .ddn(ddn)
        .path(path);
    }
    
    @Override
     public CopyOfBuilder nomeAnimale(String nomeAnimale) {
      return (CopyOfBuilder) super.nomeAnimale(nomeAnimale);
    }
    
    @Override
     public CopyOfBuilder idGoogleUtente(String idGoogleUtente) {
      return (CopyOfBuilder) super.idGoogleUtente(idGoogleUtente);
    }
    
    @Override
     public CopyOfBuilder tipologia(String tipologia) {
      return (CopyOfBuilder) super.tipologia(tipologia);
    }
    
    @Override
     public CopyOfBuilder razza(String razza) {
      return (CopyOfBuilder) super.razza(razza);
    }
    
    @Override
     public CopyOfBuilder peso(Double peso) {
      return (CopyOfBuilder) super.peso(peso);
    }
    
    @Override
     public CopyOfBuilder ddn(String ddn) {
      return (CopyOfBuilder) super.ddn(ddn);
    }
    
    @Override
     public CopyOfBuilder path(String path) {
      return (CopyOfBuilder) super.path(path);
    }
  }
  
}

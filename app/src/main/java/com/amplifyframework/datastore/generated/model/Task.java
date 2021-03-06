package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;
import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/**
 * This is an auto generated class representing the Task type in your schema.
 */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Tasks")
@Index(name = "taskItem", fields = {"taskId"})
public final class Task implements Model {
  public static final QueryField ID = field("Task", "id");
  public static final QueryField TITLE = field("Task", "title");
  public static final QueryField DESCRIPTION = field("Task", "description");
  public static final QueryField STATUS = field("Task", "status");
  public static final QueryField FILE_NAME = field("Task", "fileName");
  public static final QueryField LOCATION_LATITUDE = field("Task", "locationLatitude");
  public static final QueryField LOCATION_LONGITUDE = field("Task", "locationLongitude");
  public static final QueryField TEAM = field("Task", "taskId");
  private final @ModelField(targetType = "ID", isRequired = true)
  String id;
  private final @ModelField(targetType = "String", isRequired = true)
  String title;
  private final @ModelField(targetType = "String")
  String description;
  private final @ModelField(targetType = "String")
  String status;
  private final @ModelField(targetType = "String")
  String fileName;
  private final @ModelField(targetType = "Float")
  Double locationLatitude;
  private final @ModelField(targetType = "Float")
  Double locationLongitude;
  private final @ModelField(targetType = "Team")
  @BelongsTo(targetName = "taskId", type = Team.class)
  Team team;
  private @ModelField(targetType = "AWSDateTime", isReadOnly = true)
  Temporal.DateTime createdAt;
  private @ModelField(targetType = "AWSDateTime", isReadOnly = true)
  Temporal.DateTime updatedAt;

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getStatus() {
    return status;
  }

  public String getFileName() {
    return fileName;
  }

  public Double getLocationLatitude() {
    return locationLatitude;
  }

  public Double getLocationLongitude() {
    return locationLongitude;
  }

  public Team getTeam() {
    return team;
  }

  public Temporal.DateTime getCreatedAt() {
    return createdAt;
  }

  public Temporal.DateTime getUpdatedAt() {
    return updatedAt;
  }

  private Task(String id, String title, String description, String status, String fileName, Double locationLatitude, Double locationLongitude, Team team) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.status = status;
    this.fileName = fileName;
    this.locationLatitude = locationLatitude;
    this.locationLongitude = locationLongitude;
    this.team = team;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj == null || getClass() != obj.getClass()) {
      return false;
    } else {
      Task task = (Task) obj;
      return ObjectsCompat.equals(getId(), task.getId()) &&
          ObjectsCompat.equals(getTitle(), task.getTitle()) &&
          ObjectsCompat.equals(getDescription(), task.getDescription()) &&
          ObjectsCompat.equals(getStatus(), task.getStatus()) &&
          ObjectsCompat.equals(getFileName(), task.getFileName()) &&
          ObjectsCompat.equals(getLocationLatitude(), task.getLocationLatitude()) &&
          ObjectsCompat.equals(getLocationLongitude(), task.getLocationLongitude()) &&
          ObjectsCompat.equals(getTeam(), task.getTeam()) &&
          ObjectsCompat.equals(getCreatedAt(), task.getCreatedAt()) &&
          ObjectsCompat.equals(getUpdatedAt(), task.getUpdatedAt());
    }
  }

  @Override
  public int hashCode() {
    return new StringBuilder()
        .append(getId())
        .append(getTitle())
        .append(getDescription())
        .append(getStatus())
        .append(getFileName())
        .append(getLocationLatitude())
        .append(getLocationLongitude())
        .append(getTeam())
        .append(getCreatedAt())
        .append(getUpdatedAt())
        .toString()
        .hashCode();
  }

  @Override
  public String toString() {
    return new StringBuilder()
        .append("Task {")
        .append("id=" + String.valueOf(getId()) + ", ")
        .append("title=" + String.valueOf(getTitle()) + ", ")
        .append("description=" + String.valueOf(getDescription()) + ", ")
        .append("status=" + String.valueOf(getStatus()) + ", ")
        .append("fileName=" + String.valueOf(getFileName()) + ", ")
        .append("locationLatitude=" + String.valueOf(getLocationLatitude()) + ", ")
        .append("locationLongitude=" + String.valueOf(getLocationLongitude()) + ", ")
        .append("team=" + String.valueOf(getTeam()) + ", ")
        .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
        .append("updatedAt=" + String.valueOf(getUpdatedAt()))
        .append("}")
        .toString();
  }

  public static TitleStep builder() {
    return new Builder();
  }

  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   *
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   * @throws IllegalArgumentException Checks that ID is in the proper format
   */
  public static Task justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
          "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new Task(
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
        title,
        description,
        status,
        fileName,
        locationLatitude,
        locationLongitude,
        team);
  }

  public interface TitleStep {
    BuildStep title(String title);
  }


  public interface BuildStep {
    Task build();

    BuildStep id(String id) throws IllegalArgumentException;

    BuildStep description(String description);

    BuildStep status(String status);

    BuildStep fileName(String fileName);

    BuildStep locationLatitude(Double locationLatitude);

    BuildStep locationLongitude(Double locationLongitude);

    BuildStep team(Team team);
  }


  public static class Builder implements TitleStep, BuildStep {
    private String id;
    private String title;
    private String description;
    private String status;
    private String fileName;
    private Double locationLatitude;
    private Double locationLongitude;
    private Team team;

    @Override
    public Task build() {
      String id = this.id != null ? this.id : UUID.randomUUID().toString();

      return new Task(
          id,
          title,
          description,
          status,
          fileName,
          locationLatitude,
          locationLongitude,
          team);
    }

    @Override
    public BuildStep title(String title) {
      Objects.requireNonNull(title);
      this.title = title;
      return this;
    }

    @Override
    public BuildStep description(String description) {
      this.description = description;
      return this;
    }

    @Override
    public BuildStep status(String status) {
      this.status = status;
      return this;
    }

    @Override
    public BuildStep fileName(String fileName) {
      this.fileName = fileName;
      return this;
    }

    @Override
    public BuildStep locationLatitude(Double locationLatitude) {
      this.locationLatitude = locationLatitude;
      return this;
    }

    @Override
    public BuildStep locationLongitude(Double locationLongitude) {
      this.locationLongitude = locationLongitude;
      return this;
    }

    @Override
    public BuildStep team(Team team) {
      this.team = team;
      return this;
    }

    /**
     * WARNING: Do not set ID when creating a new object. Leave this blank and one will be auto generated for you.
     * This should only be set when referring to an already existing object.
     *
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
    private CopyOfBuilder(String id, String title, String description, String status, String fileName, Double locationLatitude, Double locationLongitude, Team team) {
      super.id(id);
      super.title(title)
          .description(description)
          .status(status)
          .fileName(fileName)
          .locationLatitude(locationLatitude)
          .locationLongitude(locationLongitude)
          .team(team);
    }

    @Override
    public CopyOfBuilder title(String title) {
      return (CopyOfBuilder) super.title(title);
    }

    @Override
    public CopyOfBuilder description(String description) {
      return (CopyOfBuilder) super.description(description);
    }

    @Override
    public CopyOfBuilder status(String status) {
      return (CopyOfBuilder) super.status(status);
    }

    @Override
    public CopyOfBuilder fileName(String fileName) {
      return (CopyOfBuilder) super.fileName(fileName);
    }

    @Override
    public CopyOfBuilder locationLatitude(Double locationLatitude) {
      return (CopyOfBuilder) super.locationLatitude(locationLatitude);
    }

    @Override
    public CopyOfBuilder locationLongitude(Double locationLongitude) {
      return (CopyOfBuilder) super.locationLongitude(locationLongitude);
    }

    @Override
    public CopyOfBuilder team(Team team) {
      return (CopyOfBuilder) super.team(team);
    }
  }

}

package br.com.supera.feedback360.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.feedback360.domain.FeedbackForm} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeedbackFormDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String title;

    private String description;

    @NotNull(message = "must not be null")
    private String status;

    @NotNull(message = "must not be null")
    private Instant createdAt;

    private Instant updatedAt;

    private SysUserDTO creator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public SysUserDTO getCreator() {
        return creator;
    }

    public void setCreator(SysUserDTO creator) {
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeedbackFormDTO)) {
            return false;
        }

        FeedbackFormDTO feedbackFormDTO = (FeedbackFormDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, feedbackFormDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeedbackFormDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", creator=" + getCreator() +
            "}";
    }
}

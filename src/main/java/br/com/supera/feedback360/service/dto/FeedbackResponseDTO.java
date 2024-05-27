package br.com.supera.feedback360.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.feedback360.domain.FeedbackResponse} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeedbackResponseDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String responseData;

    @NotNull(message = "must not be null")
    private Instant submittedAt;

    private FeedbackFormDTO form;

    private SysUserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public FeedbackFormDTO getForm() {
        return form;
    }

    public void setForm(FeedbackFormDTO form) {
        this.form = form;
    }

    public SysUserDTO getUser() {
        return user;
    }

    public void setUser(SysUserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeedbackResponseDTO)) {
            return false;
        }

        FeedbackResponseDTO feedbackResponseDTO = (FeedbackResponseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, feedbackResponseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeedbackResponseDTO{" +
            "id=" + getId() +
            ", responseData='" + getResponseData() + "'" +
            ", submittedAt='" + getSubmittedAt() + "'" +
            ", form=" + getForm() +
            ", user=" + getUser() +
            "}";
    }
}

package br.com.supera.feedback360.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A FeedbackResponse.
 */
@Table("feedback_response")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeedbackResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("response_data")
    private String responseData;

    @NotNull(message = "must not be null")
    @Column("submitted_at")
    private Instant submittedAt;

    @Transient
    @JsonIgnoreProperties(value = { "creator" }, allowSetters = true)
    private FeedbackForm form;

    @Transient
    @JsonIgnoreProperties(value = { "role" }, allowSetters = true)
    private SysUser user;

    @Column("form_id")
    private Long formId;

    @Column("user_id")
    private Long userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FeedbackResponse id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResponseData() {
        return this.responseData;
    }

    public FeedbackResponse responseData(String responseData) {
        this.setResponseData(responseData);
        return this;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public Instant getSubmittedAt() {
        return this.submittedAt;
    }

    public FeedbackResponse submittedAt(Instant submittedAt) {
        this.setSubmittedAt(submittedAt);
        return this;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public FeedbackForm getForm() {
        return this.form;
    }

    public void setForm(FeedbackForm feedbackForm) {
        this.form = feedbackForm;
        this.formId = feedbackForm != null ? feedbackForm.getId() : null;
    }

    public FeedbackResponse form(FeedbackForm feedbackForm) {
        this.setForm(feedbackForm);
        return this;
    }

    public SysUser getUser() {
        return this.user;
    }

    public void setUser(SysUser sysUser) {
        this.user = sysUser;
        this.userId = sysUser != null ? sysUser.getId() : null;
    }

    public FeedbackResponse user(SysUser sysUser) {
        this.setUser(sysUser);
        return this;
    }

    public Long getFormId() {
        return this.formId;
    }

    public void setFormId(Long feedbackForm) {
        this.formId = feedbackForm;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long sysUser) {
        this.userId = sysUser;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeedbackResponse)) {
            return false;
        }
        return getId() != null && getId().equals(((FeedbackResponse) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeedbackResponse{" +
            "id=" + getId() +
            ", responseData='" + getResponseData() + "'" +
            ", submittedAt='" + getSubmittedAt() + "'" +
            "}";
    }
}

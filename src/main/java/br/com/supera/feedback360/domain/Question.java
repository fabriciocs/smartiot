package br.com.supera.feedback360.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Question.
 */
@Table("question")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("question_text")
    private String questionText;

    @NotNull(message = "must not be null")
    @Column("question_type")
    private String questionType;

    @Transient
    @JsonIgnoreProperties(value = { "creator" }, allowSetters = true)
    private FeedbackForm feedbackForm;

    @Column("feedback_form_id")
    private Long feedbackFormId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Question id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return this.questionText;
    }

    public Question questionText(String questionText) {
        this.setQuestionText(questionText);
        return this;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionType() {
        return this.questionType;
    }

    public Question questionType(String questionType) {
        this.setQuestionType(questionType);
        return this;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public FeedbackForm getFeedbackForm() {
        return this.feedbackForm;
    }

    public void setFeedbackForm(FeedbackForm feedbackForm) {
        this.feedbackForm = feedbackForm;
        this.feedbackFormId = feedbackForm != null ? feedbackForm.getId() : null;
    }

    public Question feedbackForm(FeedbackForm feedbackForm) {
        this.setFeedbackForm(feedbackForm);
        return this;
    }

    public Long getFeedbackFormId() {
        return this.feedbackFormId;
    }

    public void setFeedbackFormId(Long feedbackForm) {
        this.feedbackFormId = feedbackForm;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return getId() != null && getId().equals(((Question) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", questionText='" + getQuestionText() + "'" +
            ", questionType='" + getQuestionType() + "'" +
            "}";
    }
}

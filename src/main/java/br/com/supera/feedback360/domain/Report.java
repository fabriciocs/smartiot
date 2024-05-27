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
 * A Report.
 */
@Table("report")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("title")
    private String title;

    @NotNull(message = "must not be null")
    @Column("generated_at")
    private Instant generatedAt;

    @NotNull(message = "must not be null")
    @Column("content")
    private String content;

    @Transient
    @JsonIgnoreProperties(value = { "role" }, allowSetters = true)
    private SysUser generatedBy;

    @Column("generated_by_id")
    private Long generatedById;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Report id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Report title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getGeneratedAt() {
        return this.generatedAt;
    }

    public Report generatedAt(Instant generatedAt) {
        this.setGeneratedAt(generatedAt);
        return this;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getContent() {
        return this.content;
    }

    public Report content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SysUser getGeneratedBy() {
        return this.generatedBy;
    }

    public void setGeneratedBy(SysUser sysUser) {
        this.generatedBy = sysUser;
        this.generatedById = sysUser != null ? sysUser.getId() : null;
    }

    public Report generatedBy(SysUser sysUser) {
        this.setGeneratedBy(sysUser);
        return this;
    }

    public Long getGeneratedById() {
        return this.generatedById;
    }

    public void setGeneratedById(Long sysUser) {
        this.generatedById = sysUser;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Report)) {
            return false;
        }
        return getId() != null && getId().equals(((Report) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Report{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", generatedAt='" + getGeneratedAt() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}

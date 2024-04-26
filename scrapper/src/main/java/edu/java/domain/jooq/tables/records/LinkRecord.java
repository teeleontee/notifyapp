/*
 * This file is generated by jOOQ.
 */

package edu.java.domain.jooq.tables.records;

import edu.java.domain.jooq.tables.Link;
import jakarta.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.time.OffsetDateTime;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;

/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.14"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class LinkRecord extends UpdatableRecordImpl<LinkRecord>
    implements Record4<Integer, String, String, OffsetDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>LINK.ID</code>.
     */
    public void setId(@Nullable Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>LINK.ID</code>.
     */
    @Nullable
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>LINK.URL</code>.
     */
    public void setUrl(@NotNull String value) {
        set(1, value);
    }

    /**
     * Getter for <code>LINK.URL</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 1000000000)
    @NotNull
    public String getUrl() {
        return (String) get(1);
    }

    /**
     * Setter for <code>LINK.CONTENT</code>.
     */
    public void setContent(@Nullable String value) {
        set(2, value);
    }

    /**
     * Getter for <code>LINK.CONTENT</code>.
     */
    @Size(max = 1000000000)
    @Nullable
    public String getContent() {
        return (String) get(2);
    }

    /**
     * Setter for <code>LINK.CHECKED_TIME</code>.
     */
    public void setCheckedTime(@NotNull OffsetDateTime value) {
        set(3, value);
    }

    /**
     * Getter for <code>LINK.CHECKED_TIME</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public OffsetDateTime getCheckedTime() {
        return (OffsetDateTime) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row4<Integer, String, String, OffsetDateTime> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row4<Integer, String, String, OffsetDateTime> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Integer> field1() {
        return Link.LINK.ID;
    }

    @Override
    @NotNull
    public Field<String> field2() {
        return Link.LINK.URL;
    }

    @Override
    @NotNull
    public Field<String> field3() {
        return Link.LINK.CONTENT;
    }

    @Override
    @NotNull
    public Field<OffsetDateTime> field4() {
        return Link.LINK.CHECKED_TIME;
    }

    @Override
    @Nullable
    public Integer component1() {
        return getId();
    }

    @Override
    @NotNull
    public String component2() {
        return getUrl();
    }

    @Override
    @Nullable
    public String component3() {
        return getContent();
    }

    @Override
    @NotNull
    public OffsetDateTime component4() {
        return getCheckedTime();
    }

    @Override
    @Nullable
    public Integer value1() {
        return getId();
    }

    @Override
    @NotNull
    public String value2() {
        return getUrl();
    }

    @Override
    @Nullable
    public String value3() {
        return getContent();
    }

    @Override
    @NotNull
    public OffsetDateTime value4() {
        return getCheckedTime();
    }

    @Override
    @NotNull
    public LinkRecord value1(@Nullable Integer value) {
        setId(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value2(@NotNull String value) {
        setUrl(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value3(@Nullable String value) {
        setContent(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value4(@NotNull OffsetDateTime value) {
        setCheckedTime(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord values(
        @Nullable Integer value1,
        @NotNull String value2,
        @Nullable String value3,
        @NotNull OffsetDateTime value4
    ) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached LinkRecord
     */
    public LinkRecord() {
        super(Link.LINK);
    }

    /**
     * Create a detached, initialised LinkRecord
     */
    @ConstructorProperties({"id", "url", "content", "checkedTime"})
    public LinkRecord(
        @Nullable Integer id,
        @NotNull String url,
        @Nullable String content,
        @NotNull OffsetDateTime checkedTime
    ) {
        super(Link.LINK);

        setId(id);
        setUrl(url);
        setContent(content);
        setCheckedTime(checkedTime);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised LinkRecord
     */
    public LinkRecord(edu.java.domain.jooq.tables.pojos.Link value) {
        super(Link.LINK);

        if (value != null) {
            setId(value.getId());
            setUrl(value.getUrl());
            setContent(value.getContent());
            setCheckedTime(value.getCheckedTime());
            resetChangedOnNotNull();
        }
    }
}
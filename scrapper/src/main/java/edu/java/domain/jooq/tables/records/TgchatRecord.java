/*
 * This file is generated by jOOQ.
 */

package edu.java.domain.jooq.tables.records;

import edu.java.domain.jooq.tables.Tgchat;
import java.beans.ConstructorProperties;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Row1;
import org.jooq.impl.TableRecordImpl;

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
public class TgchatRecord extends TableRecordImpl<TgchatRecord> implements Record1<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>TGCHAT.ID</code>.
     */
    public void setId(@NotNull Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>TGCHAT.ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getId() {
        return (Long) get(0);
    }

    // -------------------------------------------------------------------------
    // Record1 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row1<Long> fieldsRow() {
        return (Row1) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row1<Long> valuesRow() {
        return (Row1) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Long> field1() {
        return Tgchat.TGCHAT.ID;
    }

    @Override
    @NotNull
    public Long component1() {
        return getId();
    }

    @Override
    @NotNull
    public Long value1() {
        return getId();
    }

    @Override
    @NotNull
    public TgchatRecord value1(@NotNull Long value) {
        setId(value);
        return this;
    }

    @Override
    @NotNull
    public TgchatRecord values(@NotNull Long value1) {
        value1(value1);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TgchatRecord
     */
    public TgchatRecord() {
        super(Tgchat.TGCHAT);
    }

    /**
     * Create a detached, initialised TgchatRecord
     */
    @ConstructorProperties({"id"})
    public TgchatRecord(@NotNull Long id) {
        super(Tgchat.TGCHAT);

        setId(id);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised TgchatRecord
     */
    public TgchatRecord(edu.java.domain.jooq.tables.pojos.Tgchat value) {
        super(Tgchat.TGCHAT);

        if (value != null) {
            setId(value.getId());
            resetChangedOnNotNull();
        }
    }
}

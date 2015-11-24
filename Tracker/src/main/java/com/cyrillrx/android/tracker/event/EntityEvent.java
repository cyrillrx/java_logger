package com.cyrillrx.android.tracker.event;

import android.text.TextUtils;

/**
 * @author Cyril Leroux
 *         Created on 11/11/2015.
 */
public class EntityEvent extends TrackEvent {

    private String action;

    private int     intValue;
    private long    longValue;
    private float   floatValue;
    private boolean boolValue;

    EntityEvent() { }

    public String getAction() { return action; }

    public int getIntValue() { return intValue; }

    public long getLongValue() { return longValue; }

    public float getFloatValue() { return floatValue; }

    public boolean getBoolValue() { return boolValue; }

    public static class Builder {

        private final EntityEvent event;

        public Builder() {
            event = new EntityEvent();
        }

        public EntityEvent build() {
            if (TextUtils.isEmpty(event.category) ||
                    TextUtils.isEmpty(event.id) ||
                    TextUtils.isEmpty(event.type) ||
                    TextUtils.isEmpty(event.action)) {
                throw new IllegalStateException("Category, id, type and action are mandatory");
            }

            return event;
        }

        public Builder setCategory(String category) {
            event.category = category;
            return this;
        }

        public Builder setEntityId(String id) {
            event.id = id;
            return this;
        }

        public Builder setEntityType(String type) {
            event.type = type;
            return this;
        }

        public Builder setEntityName(String name) {
            event.name = name;
            return this;
        }

        public Builder setAction(String action) {
            event.action = action;
            return this;
        }

        public Builder setValue(int value) {
            event.intValue = value;
            return this;
        }

        public Builder setValue(long value) {
            event.longValue = value;
            return this;
        }

        public Builder setValue(float value) {
            event.floatValue = value;
            return this;
        }

        public Builder setValue(boolean value) {
            event.boolValue = value;
            return this;
        }
    }
}

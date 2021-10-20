package com.wellsfargo.rarconsumer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.event.*;

import static org.springframework.data.mongodb.core.query.SerializationUtils.serializeToJsonSafely;


public class LoggingEventListener extends AbstractMongoEventListener<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingEventListener.class);

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        LOGGER.info("onBeforeConvert: {}", event.getSource());
    }

    @Override
    public void onBeforeSave(BeforeSaveEvent<Object> event) {
        LOGGER.info("onBeforeSave: {}, {}", event.getSource(), serializeToJsonSafely(event.getDocument()));
    }

    @Override
    public void onAfterSave(AfterSaveEvent<Object> event) {
        LOGGER.info("onAfterSave: {}, {}", event.getSource(), serializeToJsonSafely(event.getDocument()));
    }

    @Override
    public void onAfterLoad(AfterLoadEvent<Object> event) {
        LOGGER.info("onAfterLoad: {}", serializeToJsonSafely(event.getDocument()));
    }

    @Override
    public void onAfterConvert(AfterConvertEvent<Object> event) {
        LOGGER.info("onAfterConvert: {}, {}", serializeToJsonSafely(event.getDocument()), event.getSource());
    }

    @Override
    public void onAfterDelete(AfterDeleteEvent<Object> event) {
        LOGGER.info("onAfterDelete: {}", serializeToJsonSafely(event.getDocument()));
    }

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Object> event) {
        LOGGER.info("onBeforeDelete: {}", serializeToJsonSafely(event.getDocument()));
    }
}

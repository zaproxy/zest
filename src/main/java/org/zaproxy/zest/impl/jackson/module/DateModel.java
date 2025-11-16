/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.impl.jackson.module;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.io.Serial;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateModel extends SimpleModule {
    @Serial private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);

        SimpleDeserializers desers = new SimpleDeserializers();
        desers.addDeserializer(Date.class, DateDeserializer.INSTANCE);

        context.addDeserializers(desers);

        SimpleSerializers sers = new SimpleSerializers();
        sers.addSerializer(Date.class, DateSerializer.INSTANCE);

        context.addSerializers(sers);
    }

    private static class DateDeserializer extends StdDeserializer<Date> {
        public static final DateDeserializer INSTANCE = new DateDeserializer();

        @Serial private static final long serialVersionUID = 1L;

        public DateDeserializer() {
            this(Date.class);
        }

        public DateDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException, JacksonException {
            String date = p.getText();
            return new Date(FORMATTER.parse(date, Instant::from).toEpochMilli());
        }
    }

    private static class DateSerializer extends StdSerializer<Date> {
        public static final DateSerializer INSTANCE = new DateSerializer();

        @Serial private static final long serialVersionUID = 1L;

        public DateSerializer() {
            this(Date.class);
        }

        public DateSerializer(Class<Date> t) {
            super(t);
        }

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider provider)
                throws IOException {
            gen.writeString(FORMATTER.format(value.toInstant().atOffset(ZoneOffset.UTC)));
        }
    }
}

package com.alianza.test.cm.configuration;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeDeserializer extends JSR310DateTimeDeserializerBase<ZonedDateTime> {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DEFAULT_FORMATTER;
    public static final ZonedDateTimeDeserializer INSTANCE;

    protected ZonedDateTimeDeserializer() {
        this(DEFAULT_FORMATTER);
    }

    public ZonedDateTimeDeserializer(DateTimeFormatter formatter) {
        super(ZonedDateTime.class, formatter);
    }

    protected ZonedDateTimeDeserializer(ZonedDateTimeDeserializer base, Boolean leniency) {
        super(base, leniency);
    }

    protected ZonedDateTimeDeserializer withDateFormat(DateTimeFormatter formatter) {
        return new ZonedDateTimeDeserializer(formatter);
    }

    protected ZonedDateTimeDeserializer withLeniency(Boolean leniency) {
        return new ZonedDateTimeDeserializer(this, leniency);
    }

    protected ZonedDateTimeDeserializer withShape(JsonFormat.Shape shape) {
        return this;
    }

    public ZonedDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.hasTokenId(6)) {
            return this._fromString(parser, context, parser.getText());
        } else if (parser.isExpectedStartObjectToken()) {
            return this._fromString(parser, context, context.extractScalarFromObject(parser, this, this.handledType()));
        } else {
            if (parser.isExpectedStartArrayToken()) {
                JsonToken t = parser.nextToken();
                if (t == JsonToken.END_ARRAY) {
                    return null;
                }

                if ((t == JsonToken.VALUE_STRING || t == JsonToken.VALUE_EMBEDDED_OBJECT) && context.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                    ZonedDateTime parsed = this.deserialize(parser, context);
                    if (parser.nextToken() != JsonToken.END_ARRAY) {
                        this.handleMissingEndArrayForSingle(parser, context);
                    }

                    return parsed;
                }

                if (t == JsonToken.VALUE_NUMBER_INT) {
                    int year = parser.getIntValue();
                    int month = parser.nextIntValue(-1);
                    int day = parser.nextIntValue(-1);
                    int hour = parser.nextIntValue(-1);
                    int minute = parser.nextIntValue(-1);
                    t = parser.nextToken();
                    LocalDateTime result;
                    if (t == JsonToken.END_ARRAY) {
                        result = LocalDateTime.of(year, month, day, hour, minute);
                    } else {
                        int second = parser.getIntValue();
                        t = parser.nextToken();
                        if (t == JsonToken.END_ARRAY) {
                            result = LocalDateTime.of(year, month, day, hour, minute, second);
                        } else {
                            int partialSecond = parser.getIntValue();
                            if (partialSecond < 1000 && !context.isEnabled(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)) {
                                partialSecond *= 1000000;
                            }

                            if (parser.nextToken() != JsonToken.END_ARRAY) {
                                throw context.wrongTokenException(parser, this.handledType(), JsonToken.END_ARRAY, "Expected array to end");
                            }

                            result = LocalDateTime.of(year, month, day, hour, minute, second, partialSecond);
                        }
                    }

                    return ZonedDateTime.of(result, ZoneId.systemDefault());
                }

                context.reportInputMismatch(this.handledType(), "Unexpected token (%s) within Array, expected VALUE_NUMBER_INT", new Object[]{t});
            }

            if (parser.hasToken(JsonToken.VALUE_EMBEDDED_OBJECT)) {
                return (ZonedDateTime)parser.getEmbeddedObject();
            } else {
                if (parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
                    this._throwNoNumericTimestampNeedTimeZone(parser, context);
                }

                return (ZonedDateTime)this._handleUnexpectedToken(context, parser, "Expected array or string.", new Object[0]);
            }
        }
    }

    protected ZonedDateTime _fromString(JsonParser p, DeserializationContext ctxt, String string0) throws IOException {
        String string = string0.trim();
        if (string.length() == 0) {
            return (ZonedDateTime)this._fromEmptyString(p, ctxt, string);
        } else {
            try {
                if (this._formatter == DEFAULT_FORMATTER && string.length() > 10 && string.charAt(10) == 'T' && string.endsWith("Z")) {
                    if (this.isLenient()) {
                        return ZonedDateTime.parse(string.substring(0, string.length() - 1), this._formatter);
                    } else {
                        JavaType t = this.getValueType(ctxt);
                        return (ZonedDateTime)ctxt.handleWeirdStringValue(t.getRawClass(), string, "Should not contain offset when 'strict' mode set for property or type (enable 'lenient' handling to allow)", new Object[0]);
                    }
                } else {
                    return ZonedDateTime.parse(string, this._formatter);
                }
            } catch (DateTimeException e) {
                return (ZonedDateTime)this._handleDateTimeException(ctxt, e, string);
            }
        }
    }

    static {
        DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        INSTANCE = new ZonedDateTimeDeserializer();
    }
}
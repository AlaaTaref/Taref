package com.lendico.util;

import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import org.springframework.format.datetime.standard.InstantFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.lendico.exception.InstantDateFormatException;

public class CustomInstantDeserializer extends JsonDeserializer<Instant> {
	@Override
	public Instant deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException {

		String value = jsonparser.readValueAs(String.class);
		try 
		{
			return new InstantFormatter().parse(value, Locale.getDefault());

		} 
		catch (DateTimeParseException | ParseException e) {
			throw new InstantDateFormatException("StartDate should be with format YYYY-MM-DDThh:mm:ssZ");
		}
	}
}
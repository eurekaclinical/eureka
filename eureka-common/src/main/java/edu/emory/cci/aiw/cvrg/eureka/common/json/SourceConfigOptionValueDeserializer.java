package edu.emory.cci.aiw.cvrg.eureka.common.json;

import java.io.IOException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.deser.std.UntypedObjectDeserializer;

/**
 * Acts like the default deserializer for untyped objects, except if the 
 * default deserializer returns an {@link Integer}, convert into a 
 * {@link Long}.
 * 
 * @author Andrew Post
 */
public class SourceConfigOptionValueDeserializer extends UntypedObjectDeserializer {

	@Override
	public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
		Object out = super.deserializeWithType(jp, ctxt, typeDeserializer);
        if (out instanceof Integer) {
            return Long.valueOf((Integer)out).longValue();
        }
        return out;
	}

	@Override
	public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Object out = super.deserialize(jp, ctxt);
        if (out instanceof Integer) {
            return Long.valueOf((Integer)out).longValue();
        }
        return out;
	}
	
}

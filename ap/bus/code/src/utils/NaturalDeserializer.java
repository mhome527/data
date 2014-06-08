package app.infobus.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ThangTB
 * @since 03/08/2012
 *	
 *	parser Json to hasmap
 *
 */
public class NaturalDeserializer implements JsonDeserializer<Object>{

	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public Object deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		// TODO Auto-generated method stub
		if(json.isJsonNull()) return null;
		else if(json.isJsonPrimitive()) return handlePrimitive(json.getAsJsonPrimitive());
	    else if(json.isJsonArray()) return handleArray(json.getAsJsonArray(), context);
	    else return handleObject(json.getAsJsonObject(), context);
	}
	
	
	/**
	 * The messiness inside the handlePrimitive method is for making sure you
	 *  only ever get a Double or an Integer or a Long,
	 *  and probably could be better, or at least simplified if you're okay with getting BigDecimals,
	 *   which I believe is the default.
	 *   
	 * @param json
	 * @return
	 */
	private Object handlePrimitive(JsonPrimitive json) {
		if(json.isBoolean())
			return json.getAsBoolean();
	    else if(json.isString())
	    	return json.getAsString();
	    else {
	    	BigDecimal bigDec = json.getAsBigDecimal();
	    	// Find out if it is an int type
	    	try {
	    		bigDec.toBigIntegerExact();
	    		try { 
	    			return bigDec.intValueExact(); 
	    			}
	    		catch(ArithmeticException e) {
	    			
	    		}
	    		return bigDec.longValue();
	    	} catch(ArithmeticException e) {
	    	  
	      	}
	      // Just return it as a double
	    	return bigDec.doubleValue();
	    }
	}
	
	
	  /**
	   * return Array if current position is JsonArray 
	   * 
	   * @param json
	   * @param context
	   * @return
	   */
	private Object handleArray(JsonArray json, JsonDeserializationContext context) {
		  Object[] array = new Object[json.size()];
		  for(int i = 0; i < array.length; i++)
			  array[i] = context.deserialize(json.get(i), Object.class);
		  return array;
	}
		
	/**
	 * 
	 * return map by json
	 * 
	 * @param json
	 * @param context
	 * @return
	 */
	private Object handleObject(JsonObject json, JsonDeserializationContext context) {
		Map<String, Object> map = new HashMap<String, Object>();
		for(Map.Entry<String, JsonElement> entry : json.entrySet())
			map.put(entry.getKey(), context.deserialize(entry.getValue(), Object.class));
			return map;
	}


}

/**
 * *****************************************************************************
 * Copyright (C) 2017 ELIXIR ES, Spanish National Bioinformatics Institute (INB)
 * and Barcelona Supercomputing Center (BSC)
 *
 * Modifications to the initial code base are copyright of their respective
 * authors, or their employers as appropriate.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 *****************************************************************************
 */

package es.elixir.bsc.json.schema.model.impl;

import es.elixir.bsc.json.schema.JsonSchemaException;
import es.elixir.bsc.json.schema.JsonSchemaLocator;
import es.elixir.bsc.json.schema.JsonSchemaParser;
import es.elixir.bsc.json.schema.ParsingError;
import es.elixir.bsc.json.schema.ParsingMessage;
import es.elixir.bsc.json.schema.model.JsonDefinitions;
import es.elixir.bsc.json.schema.model.JsonObjectSchema;
import es.elixir.bsc.json.schema.model.JsonSchema;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.json.JsonObject;
import javax.json.JsonValue;

/**
 * @author Dmitry Repchevsky
 */

public class JsonDefinitionsImpl extends LinkedHashMap<String, JsonSchema>
                                 implements JsonDefinitions {
    
    @Override
    public JsonSchema get(String name) {
        return super.get(name);
    }

    @Override
    public JsonSchema put(String name, JsonSchema schema) {
        return super.put(name, schema);
    }
    
    @Override
    public JsonSchema remove(String name) {
        return super.remove(name);
    }
    
    @Override
    public Iterator<Entry<String, JsonSchema>> iterator() {
        return entrySet().iterator();
    }
    
    public JsonDefinitions read(JsonSchemaParser parser, JsonSchemaLocator locator, String jsonPointer, JsonObject object) throws JsonSchemaException {
        
        for (Map.Entry<String, JsonValue> entry : object.entrySet()) {
            final JsonValue value = entry.getValue();            
            if (JsonValue.ValueType.OBJECT != value.getValueType()) {
                throw new JsonSchemaException(new ParsingError(ParsingMessage.INVALID_OBJECT_TYPE, 
                    new Object[] {"definition schema", value.getValueType().name(), JsonValue.ValueType.OBJECT.name()}));
            }
            
            final JsonSchema schema = parser.parse(locator, jsonPointer + entry.getKey() + "/", value.asJsonObject());

            locator.getSchemas().put("#/" + JsonObjectSchema.DEFINITIONS + "/" + entry.getKey(), value.asJsonObject());
            put(entry.getKey(), schema);
        }
        
        return this;
    }
}

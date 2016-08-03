package services;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface IMongoService<T> {
	String save(String t) throws JsonGenerationException, JsonMappingException, IOException;
	String get(String key);
	List<String> getAll();
}

package staging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sandbox.model.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class PojoToJson {
    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Long> ids = new ArrayList<>();
        ids.add(1l);
        SearchResult searchResult = new SearchResult(ids, 0);
        System.out.println(mapper.writeValueAsString(searchResult));
    }
}

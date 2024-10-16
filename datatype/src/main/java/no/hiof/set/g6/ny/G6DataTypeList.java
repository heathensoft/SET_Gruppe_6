package no.hiof.set.g6.ny;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

/**
 * @author Frederik Dahl
 * 16/10/2024
 */


public class G6DataTypeList<T extends G6Datatype> implements G6Serializable {
    
    private List<T> list;
    
    
    
    @Override
    public void fromJson(JSONObject jsonObject) throws Exception {
    
    }
    
    @Override
    public JSONObject toJson() {
        return null;
    }
}

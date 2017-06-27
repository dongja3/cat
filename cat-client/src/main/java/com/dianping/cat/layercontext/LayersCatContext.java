package com.dianping.cat.layercontext;

import com.dianping.cat.Cat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DONGJA3 on 6/20/2017.
 */
public class LayersCatContext implements Cat.Context {
    public static final String CAT_URL= "cat_url";
    public static final String CAT_LAYER_CONTEXT="cat_layer_context";
    private Map<String, String> properties = new HashMap<String, String>();

    @Override
    public void addProperty(String key, String value) {
        properties.put(key,value);
    }

    @Override
    public String getProperty(String key) {
        return properties.get(key);
    }

    public String getCatUrl (){
        return getProperty(CAT_URL);
    }
}

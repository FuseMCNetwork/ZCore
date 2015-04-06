package net.fusemc.zcore.featureSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marco on 28.05.2014.
 */
public class FeatureManager {

    public List<Feature> features = new ArrayList<>();

    public FeatureManager(){
        this.features = new ArrayList<Feature>();
    }

    public <T> T getFeature(Class<T> clazz){
        for(Feature feature: features){
            if(feature.getClass().equals(clazz)){
                return (T) feature;
            }
        }
        try {
            T feature = clazz.newInstance();
            if(feature instanceof Feature){
                this.features.add((Feature) feature);
                return feature;
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            throw new FeatureException("Invalid constructor");
        } catch (FeatureException e){
            e.printStackTrace();
        }
        return null;
    }

    public void serverStop(){
        for(Feature feature: features){
            if(feature == null){
                continue;
            }
            feature.onServerStop();
        }
    }
}

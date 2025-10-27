package uq.comp3506.a2.structures;

import java.util.List;
import java.util.ArrayList;

public class LogicVertex<S> extends Vertex<S>{

    private List<Integer> requirements;
    /**
     * Simple constructor taking an identifier and some data
     *
     * @param id
     * @param data
     */
    public LogicVertex(int id, S data) {
        super(id, data);
        this.requirements = new ArrayList<>();
    }

    public List<Integer> getRequirements(){
        return this.requirements;
    }

    public void setRequirements(List<Integer> newreq){
        this.requirements = newreq;
    }

    public void addRequirement(int req){
        this.requirements.add(req);
    }
}

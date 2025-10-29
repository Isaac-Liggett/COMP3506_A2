package uq.comp3506.a2.structures;

import java.util.List;
import java.util.ArrayList;

/**
 * A logic vertex used to store prerequisite conditions for activating the node.
 * @param <S> - data stored in vertex
 */
public class LogicVertex<S> extends Vertex<S> {

    private List<Integer> requirements;

    /**
     * Simple constructor taking an identifier and some data
     *
     * @param id - the id of the node
     * @param data - the data stored by the node
     */
    public LogicVertex(int id, S data) {
        super(id, data);
        this.requirements = new ArrayList<>();
    }

    /**
     * Gets the requirements for the node.
     * @return the requirements of this node
     */
    public List<Integer> getRequirements() {
        return this.requirements;
    }

    /**
     * Sets the requirements of the node.
     * @param newreq - new requirements to store.
     */
    public void setRequirements(List<Integer> newreq) {
        this.requirements = newreq;
    }

    /**
     * Adds another requirement onto node requirements
     * @param req - requirement to add
     */
    public void addRequirement(int req) {
        this.requirements.add(req);
    }
}

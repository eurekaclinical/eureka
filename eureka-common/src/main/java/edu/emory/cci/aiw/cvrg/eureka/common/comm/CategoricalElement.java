package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import java.util.List;

/**
 * Container class for the categorical user-created data element from the UI.
 * Essentially a direct mapping form the categorical element form fields.
 */
public final class CategoricalElement extends DataElement {
    private List<DataElement> children;

    public List<DataElement> getChildren() {
        return children;
    }

    public void setChildren(List<DataElement> children) {
        this.children = children;
    }
}

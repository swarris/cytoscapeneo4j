package nl.corwur.cytoscape.neo4j.internal.graph.commands;


public class NodeLabel extends Label {

	public NodeLabel(String label) {
		super(label);
	}

	public static Label create(String label) {
        if(label.matches("[\\w\\d\\s]+")) {
            return new NodeLabel(label);
        }
        throw new IllegalStateException();
     }

}

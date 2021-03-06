package nl.corwur.cytoscape.neo4j.internal.ui.exportnetwork;

import nl.corwur.cytoscape.neo4j.internal.Services;
import nl.corwur.cytoscape.neo4j.internal.commands.tasks.exportneo4j.ExportNetworkConfiguration;
import nl.corwur.cytoscape.neo4j.internal.graph.commands.NetworkLabel;
import nl.corwur.cytoscape.neo4j.internal.commands.tasks.ExportNetworkToNeo4jTask;
import nl.corwur.cytoscape.neo4j.internal.ui.DialogMethods;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.model.CyNetwork;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ExportNetworkMenuAction extends AbstractCyAction {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3105483618300742403L;
	private static final String MENU_TITLE = "Export Network to Neo4j";
    private static final String MENU_LOC = "Apps.Cypher Queries";

    private final transient Services services;

    public static ExportNetworkMenuAction create(Services services) {
        return new ExportNetworkMenuAction(services);
    }

    private ExportNetworkMenuAction(Services services) {
        super(MENU_TITLE);
        this.services = services;
        setPreferredMenu(MENU_LOC);
        setEnabled(false);
        setMenuGravity(0.5f);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(!DialogMethods.connect(services)) {
            return;
        }

        NetworkLabel networkLabel = getNetworkLabel();
        ExportNetworkConfiguration exportNetworkConfiguration = ExportNetworkConfiguration.create(networkLabel);
        if (networkLabel != null) {
            ExportNetworkToNeo4jTask task = services.getCommandFactory().createExportNetworkToNeo4jTask(exportNetworkConfiguration);
            services.getCommandExecutor().execute(task);
        }
    }

    private NetworkLabel getNetworkLabel() {
        String message = "Enter label for this network";
        CyNetwork currentNetwork = services.getCyApplicationManager().getCurrentNetwork();
        String initialValue = currentNetwork.getRow(currentNetwork).get(CyNetwork.NAME, String.class);

        while (true) {
            String label = JOptionPane.showInputDialog(services.getCySwingApplication().getJFrame(), message, initialValue);
            if (label != null) {
                try {
                    return (NetworkLabel) NetworkLabel.create(label);
                } catch (Exception e) {
                    message = "Error in network label ([A-Za-z0-9 ])";
                }
            } else {
                return null;
            }
        }
    }
}

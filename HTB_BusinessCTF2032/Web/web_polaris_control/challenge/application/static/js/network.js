window.onload = () => {
    setTimeout(() => {
        document.getElementById("loadingSection").classList.add("hide");
    }, 3000);

    let container = document.getElementById("network");
    let data = {
        nodes: new vis.DataSet(connections),
        edges: new vis.DataSet(edges)
    };

    let options = {
        edges: {
            length: 500
        },
        physics: {
            solver: "repulsion",
            repulsion: {
                nodeDistance: 500
            }
        }
    };

    let network = new vis.Network(container, data, options);

    let allNodes = data.nodes.get();

    let connectedNodeIds = new Set();
    data.edges.forEach((edge) => {
        connectedNodeIds.add(edge.from);
        connectedNodeIds.add(edge.to);
    });

    let disconnectedNodes = allNodes.filter((node) => {
        return !connectedNodeIds.has(node.id);
    });

    data.nodes.remove(disconnectedNodes);
    network.setData(data);
}
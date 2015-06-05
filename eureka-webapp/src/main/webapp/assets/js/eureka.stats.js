var labelType, useGradients, nativeTextSupport, animate;

(function () {
	var ua = navigator.userAgent,
			iStuff = ua.match(/iPhone/i) || ua.match(/iPad/i),
			typeOfCanvas = typeof HTMLCanvasElement,
			nativeCanvasSupport = (typeOfCanvas == 'object' || typeOfCanvas == 'function'),
			textSupport = nativeCanvasSupport
			&& (typeof document.createElement('canvas').getContext('2d').fillText == 'function');
	//I'm setting this based on the fact that ExCanvas provides text support for IE
	//and that as of today iPhone/iPad current text support is lame
	labelType = (!nativeCanvasSupport || (textSupport && !iStuff)) ? 'Native' : 'HTML';
	nativeTextSupport = labelType == 'Native';
	useGradients = nativeCanvasSupport;
	animate = !(iStuff || !nativeCanvasSupport);
})();

var Log = {
	elem: false,
	write: function (text) {
		if (!this.elem)
			this.elem = document.getElementById('log');
		this.elem.innerHTML = text;
		this.elem.style.left = (500 - this.elem.offsetWidth / 2) + 'px';
	}
};


function init(jobId) {
	$('#msg').hide();
	$('#loading').show();

	//init TreeMap
	var tm = new $jit.TM.Squarified({
		//where to inject the visualization
		injectInto: 'infovis',
		//show only one tree level
		levelsToShow: 1,
		//parent box title heights
		titleHeight: 0,
		//enable animations
		animate: animate,
		//box offsets
		offset: 1,
		//use canvas text
		Label: {
			type: labelType,
			size: 9,
			family: 'Tahoma, Verdana, Arial'
		},
		//enable specific canvas styles
		//when rendering nodes
		Node: {
			CanvasStyles: {
				shadowBlur: 0,
				shadowColor: '#000'
			}
		},
		//Attach left and right click events
		Events: {
			enable: true,
			onClick: function (node) {
				if (node)
					tm.enter(node);
			},
			onRightClick: function () {
				tm.out();
			},
			//change node styles and canvas styles
			//when hovering a node
			onMouseEnter: function (node, eventInfo) {
				if (node) {
					//add node selected styles and replot node
					node.setCanvasStyle('shadowBlur', 7);
					node.setData('color', '#888');
					tm.fx.plotNode(node, tm.canvas);
					tm.labels.plotLabel(tm.canvas, node);
				}
			},
			onMouseLeave: function (node) {
				if (node) {
					node.removeData('color');
					node.removeCanvasStyle('shadowBlur');
					tm.plot();
				}
			}
		},
		//duration of the animations
		duration: 1000,
		//Enable tips
		Tips: {
			enable: true,
			type: 'Native',
			//add positioning offsets
			offsetX: 20,
			offsetY: 20,
			//implement the onShow method to
			//add content to the tooltip when a node
			//is hovered
			onShow: function (tip, node, isLeaf, domElement) {
				var html = "<div class=\"tip-title\">" + node.name
						+ "</div><div class=\"tip-text\">";
				var data = node.data;
				html += "<div>ID: " +node.id + "</div>"
				if (data.$area) {
					html += "<div>Patient count: " + data.$area + "</div>";
				}
				html += "</div>";
				tip.innerHTML = html;
			}
		},
		//Implement this method for retrieving a requested  
		//subtree that has as root a node with id = nodeId,  
		//and level as depth. This method could also make a server-side  
		//call for the requested subtree. When completed, the onComplete   
		//callback method should be called.  
		request: function (nodeId, level, onComplete) {
			$.ajax({
				url: "jobpatcounts?jobId=" + jobId + "&propId=" + encodeURIComponent(nodeId),
				success: function (data) {
					$('#msg').hide();
					var children = [], countList = data.counts;
					for (var k = 0; k < countList.length; k++) {
						var count = countList[k];
						if (count.count > 0) {
							children.push({
								children: [],
								id: count.key,
								name: count.displayName,
								'data': {$area: count.count}
							});
						}
					}
					if (children.length > 0) {
						var node = {
							'data': {},
							children: children,
							id: nodeId,
							name: nodeId
						};
					}
					onComplete.onComplete(nodeId, node);
				},
				error: function () {
					$('#loading').hide();
					$('#msg').show();
				},
				dataType: "json"
			});
		},
		//Add the name of the node in the corresponding label
		//This method is called once, on label creation and only for DOM labels.
		onCreateLabel: function (domElement, node) {
			domElement.innerHTML = node.name;
		}
	});

	$.ajax({
		url: "jobpatcounts?jobId=" + jobId,
		success: function (data) {
			$('#loading').hide();
			$('#msg').hide();
			var children = [], countList = data.counts;
			for (var k = 0; k < countList.length; k++) {
				var count = countList[k];
				if (count.count > 0) {
					children.push({
						children: [],
						id: count.key,
						name: count.displayName,
						'data': {$area: count.count}
					});
				}
			}
			var node = {
				'data': {},
				'children': children,
				id: 'root',
				name: 'Population'
			};
			tm.loadJSON(node);
			tm.refresh();
		},
		error: function () {
			$('#msg').show();
		},
		dataType: "json"
	});
}

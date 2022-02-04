const addNodeBtn = document.getElementById('add_node')
const addArrowBtn = document.getElementById('add_arrow')
const findPathBtn = document.getElementById('find_path')
const setTargetBtn = document.getElementById('set_target')
const playground = document.getElementById('playground')

const setupPage = () => {
  addNodeBtn.addEventListener('click', addNode)
  addArrowBtn.addEventListener('click', addArrow)
  findPathBtn.addEventListener('click', findPath)
  setTargetBtn.addEventListener('click', setTargetNode)
  playground.addEventListener('click', nodeClick)

  addNode('start')
}

let nodeId = 0
const draggables = {}
const addNode = (type) => {
  nodeId++
  const newNode = document.createElement('div')
  newNode.append(nodeId)
  newNode.classList.add('node')

  if(type === 'start') {
    newNode.classList.add('start')
  }

  newNode.id = `node-${nodeId}`
  playground.append(newNode)
  draggables[newNode.id] = new PlainDraggable(document.getElementById(newNode.id), {left: 30})
  draggables[newNode.id].onDrag = redrawConnections
}

let addingArrow = false
let pickingFirst = true

const addArrow = () => {
  addingArrow = true
  pickingFirst = true
  settingTarget = false
}

let settingTarget = false
const setTargetNode = () => {
  addingArrow = false
  pickingFirst = false
  settingTarget = true  
}

let firstNode
let secondNode
let targetNode = null
const connections = {}

const nodeClick = (ev) => {
  if(!ev.target.classList.contains('node')) {
    return
  }

  if(addingArrow && pickingFirst) {
    firstNode = ev.target
    pickingFirst = false
  } else if(addingArrow && !pickingFirst) {
    secondNode = ev.target

    connections[`${firstNode.id}//${secondNode.id}`] = new LeaderLine(firstNode, secondNode, {path: 'straight', color: '#bdbdbd'})
    addingArrow = false
  } else if(settingTarget) {
    const target = document.getElementsByClassName('target')
    if(target.length > 0) {
      target[0].classList.remove('target')
    }
    ev.target.classList.add('target')
    targetNode = target[0].id
    settingTarget = false
  }
}

const redrawConnections = () => {
  Object.values(connections).forEach(c => {
    c.position()
    c.setOptions({color: '#bdbdbd'})
  })
}

const findPath = async () => {
  const nodes = {}
  Object.keys(connections).forEach(k => {
    const [firstNode, secondNode] = k.split('//')
    if(!nodes[firstNode]) {
      nodes[firstNode] = {
        id: Number(firstNode.split('-')[1]),
        connections: [],
        x: draggables[firstNode].left,
        y: draggables[firstNode].top
      }
    }

    if(!nodes[secondNode]) {
      nodes[secondNode] = {
        id: Number(secondNode.split('-')[1]),
        connections: [],
        x: draggables[secondNode].left,
        y: draggables[secondNode].top
      }
    }

    nodes[firstNode].connections.push(Number(secondNode.split('-')[1]))
  })
  
  if(targetNode == null) {
    document.getElementById('error').innerHTML = 'No target node'
    return
  }
  document.getElementById('error').innerHTML = ''
  redrawConnections()

  const data = {
    nodes: Object.values(nodes),
    target: Number(targetNode.split('-')[1])
  }


  const res = await axios.post('https://6zz7ynqprh.execute-api.us-east-1.amazonaws.com/dev/dijkstra/path/', data)

  for(let i = 0; i < res.data.path.length; i++) {
    const node = res.data.path[i]
    const nextNode = res.data.path[i+1]
    if(typeof nextNode === 'undefined') {
      break;
    }
    
    const connection = connections[`node-${node}//node-${nextNode}`]
    connection.setOptions({color: '#ffc406'})
  }
}


setupPage()
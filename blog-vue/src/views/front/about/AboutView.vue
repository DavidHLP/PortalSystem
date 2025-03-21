// Vue Flow 节点
const nodes = ref<Node[]>([
  {
    id: '1',
    type: 'service',
    position: { x: 250, y: 25 },
    data: { label: '前端应用', type: '前端' },
  },
  {
    id: '2',
    type: 'service',
    position: { x: 250, y: 125 },
    data: { label: '网关服务', type: '网关' },
  },
  {
    id: '3',
    type: 'service',
    position: { x: 100, y: 225 },
    data: { label: '认证服务', type: '认证' },
  },
  {
    id: '4',
    type: 'service',
    position: { x: 250, y: 225 },
    data: { label: '用户服务', type: '用户' },
  },
  {
    id: '5',
    type: 'service',
    position: { x: 250, y: 325 },
    data: { label: '数据库', type: '存储' },
  },
])

// Vue Flow 边
const edges = ref<Edge[]>([
  {
    id: 'e1-2',
    type: 'service',
    source: '1',
    target: '2',
    animated: true,
    data: { label: 'HTTP请求' },
  },
  {
    id: 'e2-3',
    type: 'service',
    source: '2',
    target: '3',
    animated: true,
    data: { label: '认证' },
  },
  {
    id: 'e2-4',
    type: 'service',
    source: '2',
    target: '4',
    animated: true,
    data: { label: '用户信息' },
  },
  {
    id: 'e3-5',
    type: 'service',
    source: '3',
    target: '5',
    animated: true,
    data: { label: '存储' },
  },
  {
    id: 'e4-5',
    type: 'service',
    source: '4',
    target: '5',
    animated: true,
    data: { label: '存储' },
  },
]) 
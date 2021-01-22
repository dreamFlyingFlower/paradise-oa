import service from '@utils/service';

export default {
    getTree,
    getSelfChildren
}

function getTree(dicId) {
    return service.get(`dic/getTree/${dicId}`);
}

function getSelfChildren(dicId){
    return service.get(`dic/getSelfChildren/${dicId}`);
}
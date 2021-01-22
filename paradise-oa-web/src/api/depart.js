import service from '@utils/service';

export default {
    getTree
}

function getTree(departId) {
    return service.get(`depart/getTree/${departId}`);
}
import service from '@utils/service'

export default {
    searchUser,
    transactionList
}

function searchUser(name) {
    return service.get('search/user', name);
}

function transactionList(query) {
    return service.get('transction/list', query);
}
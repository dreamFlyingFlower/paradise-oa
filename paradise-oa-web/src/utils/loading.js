import {Loading} from 'element-ui'

// 遮罩层
let needLoadingRequestCount = 0
let loading

export function showFullScreenLoading() {
    if (needLoadingRequestCount === 0) {
        startLoading()
    }
    needLoadingRequestCount++
}

export function tryHideFullScreenLoading() {
    if (needLoadingRequestCount <= 0) return
    needLoadingRequestCount--
    if (needLoadingRequestCount === 0) {
        endLoading()
    }
}

function startLoading() {
    loading = Loading.service({
        lock: true,
        text: '加载中……',
        spinner: 'el-icon-loading',
        background: 'rgba(255, 255, 255, 0.7)',
        target: document.querySelector('.el-table__body-wrapper')
    });
}

function endLoading() {
    loading.close()
}
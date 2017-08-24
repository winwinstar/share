import Alt from 'bases/Alt.js';
import IndexAction from 'actions/IndexAction.js';
import xFetch from 'services/xFetch.js';
class IndexStore{
	constructor(){
		this.bindListeners({
			handleInitTest: IndexAction.initTest,
            handleGetMoreData: IndexAction.getMoreData
		});
		this.state={
			testState:'hello test',
            dataList:[]
        }
	}

	handleInitTest = (value)=>{
		console.log('test store value:',value)
	}

    handleGetMoreData = () => {
        xFetch(SERVER_URL + '/index/getInitInfo').then(result => {
            this.setState({
                dataList: result.data,
            });
        });
    }
}

export default Alt.createStore(IndexStore, 'IndexStore');

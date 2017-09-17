import Alt from 'bases/Alt.js';
import IndexAction from 'actions/IndexAction.js';
import xFetch from 'services/xFetch.js';
import querystring from 'querystring';

class IndexStore{
	constructor(){
		this.bindListeners({
            handleGetAllMenuInfo: IndexAction.getAllMenuInfo,
            handleGetInitInfo:    IndexAction.getInitInfo,
            handleAddMenuInfo:    IndexAction.addMenuInfo,
            handleDelMenuInfo:    IndexAction.delMenuInfo,
		});
		this.state={
            menuList:[],
            dataList:[],
        }
	}

    handleGetAllMenuInfo = ()=>{
        xFetch(SERVER_URL + '/order/getAllMenuInfo.json').then(result => {
            this.setState({
                menuList: result.data,
            });
        });
	}

    handleGetInitInfo = (value) => {
        xFetch(SERVER_URL + '/order/login.json?token=' + value).then(result => {
            this.setState({
                dataList: result.data,
            });
        });
    }

    handleAddMenuInfo = (value) => {
        let orderParam = "dateIndex=" + value.dateIndex + "&"
                        + "revisionId=" + value.revisionId + "&"
                        + "name=" + value.name + "&"
                        + "corpRestaurant=" +value.corpRestaurant;
        xFetch(SERVER_URL + '/order/addOrder.json?' + orderParam).then(result => {
            this.setState({

            });
        });
    }

    handleDelMenuInfo = (value) => {
        let orderParam = "dateIndex=" + value.dateIndex;
        xFetch(SERVER_URL + '/order/delOrder.json?' + orderParam).then(result => {
            this.setState({

            });
        });
    }
}

export default Alt.createStore(IndexStore, 'IndexStore');

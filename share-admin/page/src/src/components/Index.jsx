import React,{Component} from 'react';
import connectToStores from 'alt-utils/lib/connectToStores';
import IndexStore from 'stores/IndexStore.js';
import IndexAction  from 'actions/IndexAction.js';
import Layout from 'components/common/Layout';
import {Button,Table, Modal, Checkbox, Form, Row, Col, Input, Switch, Upload, Card, Select, Rate, Icon} from 'antd';
import styles from './indexPage.less';
import {getYMDW} from 'services/functions.js';

const CheckboxGroup = Checkbox.Group;
const Option = Select.Option;

const plainOptions = ['Apple', 'Pear', 'Orange'];
const defaultCheckedList = ['Apple', 'Pear', 'Orange'];

class Index extends Component {

	static getStores(){
		return [IndexStore]
	}

	static getPropsFromStores(){
		let state = IndexStore.getState();
		console.log('Global State :',state)
		return {
            menuList: state.menuList,
            dataList: state.dataList
		}
	}

    componentDidMount() {
        let meiCangToken = document.getElementById('meiCangToken').value;
        if (meiCangToken.length === 47) {
            IndexAction.getInitInfo(meiCangToken);
        }
        var username = document.cookie.split(";")[0].split("=")[0];
        if (username.endsWith('token')) {
            IndexAction.getInitInfo(document.cookie.split(";")[0].split("=")[1]);
        }
        IndexAction.getAllMenuInfo(null);
    }

    handleInitInfo = (e) => {
        let meiCangToken = document.getElementById('meiCangToken').value;
        if (meiCangToken.length === 47) {
            var username = document.cookie.split(";")[0].split("=")[0];
            IndexAction.getInitInfo(meiCangToken);
            console.log("cookie", username);
        }
    }

    onChange(e) {
        if (e) {
            console.log("e", e);
        }
        if (!!!e) {
            this.setState({

            });
        }
    }

	render(){
	    let menuDO = this.props.dataList.menuDO;
	    let userDO = this.props.dataList.userDO;
	    let menuDOs = this.props.dataList.menuDOS;
        let menuList = this.props.menuList;
	    let timestamp = new Date();
	    let userName = "";
	    let menuView = (
            <div>
                <Col span={12}><a style={{color:"red"}}>抓紧时间还未点餐哦！</a></Col>
            </div>
        );

	    if (menuDO) {
	        timestamp = menuDO.timestamp;
            menuView = (
                <div>
                    <Col span={14} style={{color:"purple"}}>今晚已点：<a style={{color:"green"}}>{ menuDO.name }，</a>{ menuDO.location }号</Col>
                    <Col span={10}><a>发送到QQ</a></Col>
                </div>
            );
        }

        if (timestamp) {
	        let str = getYMDW(timestamp, userName);
	        if (str.indexOf("星期六") > 0 || str.indexOf("星期天") > 0) {
                menuView = (
                    <div>
                        <Col span={12}><a style={{color:"red"}}>周末就别想着加班的事了！</a></Col>
                    </div>
                );
            }
        }

        let getCookieView = (
            <Col span={12}>
                <Row>
                    <Col span={17}><Input id={'meiCangToken'} style={{width:'350px'}} placeholder="登录美餐网获取的token"/></Col>
                    <Col span={6}><Button type="primary" icon="search" onClick={this.handleInitInfo}>获取点餐信息</Button></Col>
                </Row>
            </Col>
        );
        if (userDO) {
            userName = userDO.name;
            getCookieView = null;
        }

		return(
        <div>
            <Layout>
                <Row className={styles.antRow}>
                    <Col span={12} style={{color:"blue"}}> {getYMDW(timestamp, userName)} </Col>
                    { getCookieView }
                </Row>
                <Row className={styles.antRow}>
                    { menuView }
                </Row>
                {/*<Table columns={columns} dataSource={this.props.dataList} bordered/>*/}
                <Row className={styles.antRow}>
                    <Col span={5}>星期一</Col>
                    <Col span={5}>星期二</Col>
                    <Col span={5}>星期三</Col>
                    <Col span={5}>星期四</Col>
                    <Col span={4}>星期五</Col>
                </Row>
                <AllMenus menuList={menuList} menuDOs={menuDOs}/>
            </Layout>
        </div>

		)
	}
}

class MenuInfoSecByDay extends Component {

    state = {
        selectMenuId: '',
        selectMenu: '',
        selectRestaurant: '',
    };

    handleChange(value,  menuList, index) {
        console.log(`selected ${value}`);
        if (menuList) {
            menuList.map((item) => {
                if (value === item.revisionId) {
                    this.setState({
                        selectMenuId: item.revisionId,
                        selectMenu: item.name.substr(0, item.name.indexOf("(")) || item.name,
                        selectRestaurant: item.corpRestaurant.substr(0, item.corpRestaurant.indexOf("(")) || item.corpRestaurant,
                    });
                    IndexAction.addMenuInfo({
                        dateIndex: index,
                        revisionId: item.revisionId,
                        name: item.name,
                        corpRestaurant: item.corpRestaurant,
                    });
                }
            });
        }
    }

    handleCancel(index) {
        IndexAction.delMenuInfo({
            dateIndex: index,
        }),
        this.setState({
            selectMenuId: '',
            selectMenu: '',
            selectRestaurant: '',
        });
    }

    render () {
        let menuList = this.props.menuList;
        let index = this.props.index;
        let menuDOs = this.props.menuDOs;
        let menuOption = null;

        if (menuList) {
            menuOption = menuList.map((item, index) => {
                let value = item;
                if (value.name) {
                    return <Option value={value.revisionId} key={index}>{value.name}</Option>;
                }
                return null;
            });
        }

        if (menuDOs) {
            this.state.selectMenuId = menuDOs.revisionId;
            if (menuDOs.name && menuDOs.name.indexOf("(") > 0) {
                this.state.selectMenu =  menuDOs.name.substr(0, menuDOs.name.indexOf("(")) || menuDOs.name;
            } else {
                this.state.selectMenu =  menuDOs.name;
            }
            if (menuDOs.corpRestaurant && menuDOs.corpRestaurant.indexOf("(") > 0) {
                this.state.selectRestaurant = menuDOs.corpRestaurant.substr(0, menuDOs.corpRestaurant.indexOf("(")) || menuDOs.corpRestaurant;
            } else {
                this.state.selectRestaurant = menuDOs.corpRestaurant;
            }
        }

        let cancelMenu = null;
        if (this.state.selectMenu && this.state.selectRestaurant) {
            cancelMenu = (
                <Button style={{marginTop: '10px'}} type="primary" shape="circle" icon="close" size={'large'} onClick={() => this.handleCancel(index)}/>
            );
        }
        return (
            <div>
                <PicturesWall/>
                <p>{ this.state.selectMenu || '-' }</p>
                <p>店铺：{ this.state.selectRestaurant || '-' }</p>
                <div>
                    <Select
                        showSearch
                        style={{ width: '100%' }}
                        placeholder="还没翻牌哦"
                        optionFilterProp="children"
                        onChange={(key) => this.handleChange(key,menuList,index)}
                        onFocus={this.handleFocus}
                        filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}
                    >
                        { menuOption }
                    </Select>
                </div>
                { cancelMenu }
            </div>
        )
    }

}

export default connectToStores(Index);

export class AllMenus extends Component {

    render () {
        const gridStyle = {
            width: '20%',
            height: '210px',
            textAlign: 'center',
        };

        let menuList = this.props.menuList;
        let menuDOs = this.props.menuDOs;
        let menuView = (
            <div>
                <Card.Grid style={gridStyle}><MenuInfoSecByDay index={1} menuList={menuList} /></Card.Grid>
                <Card.Grid style={gridStyle}><MenuInfoSecByDay index={2} menuList={menuList} /></Card.Grid>
                <Card.Grid style={gridStyle}><MenuInfoSecByDay index={3} menuList={menuList} /></Card.Grid>
                <Card.Grid style={gridStyle}><MenuInfoSecByDay index={4} menuList={menuList} /></Card.Grid>
                <Card.Grid style={gridStyle}><MenuInfoSecByDay index={5} menuList={menuList} /></Card.Grid>
            </div>
        );
        if (menuDOs) {
            console.log("menudo weeklist", menuDOs);
            let menuDO1 = [];
            let menuDO2 = [];
            let menuDO3 = [];
            let menuDO4 = [];
            let menuDO5 = [];
            menuDOs.map((item, index) => {
                switch (item.weekDate) {
                    case 1:
                        menuDO1 = item;
                        break;
                    case 2:
                        menuDO2 = item;
                        break;
                    case 3:
                        menuDO3 = item;
                        break;
                    case 4:
                        menuDO4 = item;
                        break;
                    case 5:
                        menuDO5 = item;
                        break;
                    default:
                        break;
                }
            });
            menuView = (
                <div>
                    <Card.Grid style={gridStyle}><MenuInfoSecByDay index={1} menuList={menuList} menuDOs={menuDO1}/></Card.Grid>
                    <Card.Grid style={gridStyle}><MenuInfoSecByDay index={2} menuList={menuList} menuDOs={menuDO2}/></Card.Grid>
                    <Card.Grid style={gridStyle}><MenuInfoSecByDay index={3} menuList={menuList} menuDOs={menuDO3}/></Card.Grid>
                    <Card.Grid style={gridStyle}><MenuInfoSecByDay index={4} menuList={menuList} menuDOs={menuDO4}/></Card.Grid>
                    <Card.Grid style={gridStyle}><MenuInfoSecByDay index={5} menuList={menuList} menuDOs={menuDO5}/></Card.Grid>
                </div>
            );
        }
        return (
             menuView
        )
    }

}

export class PicturesWall extends Component {
    state = {
        previewVisible: false,
        previewImage: '',
        fileList: [{
            uid: -1,
            name: 'xxx.png',
            status: 'done',
            url: SERVER_URL + '/swagger/images/7b1007a834cc59dc3390da1d6609fada.png',
        }],
    };

    handleCancel = () => this.setState({ previewVisible: false })

    handlePreview = (file) => {
        this.setState({
            previewImage: file.url || file.thumbUrl,
            previewVisible: true,
        });
    }

    handleChange = ({ fileList }) => this.setState({ fileList })

    render() {
        const { previewVisible, previewImage, fileList } = this.state;
        return (
            <div>
                <Upload
                    listType="picture-card"
                    fileList={fileList}
                    onPreview={this.handlePreview}
                    onChange={this.handleChange}
                    onRemove={false}
                >
                </Upload>
                <Modal visible={previewVisible} footer={null} onCancel={this.handleCancel}>
                    <img alt="example" style={{ width: '100%' }} src={previewImage} />
                </Modal>
            </div>
        );
    }
}


let ActiveConfigModal = React.createClass({
    getInitialState() {
        return {
            modalVisible: false,
            reasonVisible: false,
            checkedList: defaultCheckedList,
            indeterminate: true,
            checkAll: true
        };
    },
    setModalVisible(modalVisible) {
        this.setState({modalVisible});
    },
    handleSubmit() {
        this.props.form.validateFields((errors, values) => {
            if (!!errors) {
                console.log('Errors in form!!!');
                return;
            }
            console.log('Submit!!!', values);
            this.setModalVisible(false);
            this.props.form.resetFields();
        });
    },


    onChange (checkedList) {
        this.setState({
            checkedList,
            indeterminate: !!checkedList.length && (checkedList.length < plainOptions.length),
            checkAll: checkedList.length === plainOptions.length,
        });
    },

    onCheckAllChange (e) {
        this.setState({
            checkedList: e.target.checked ? plainOptions : [],
            indeterminate: false,
            checkAll: e.target.checked,
        });
        console.log("checkedList",checkedList);
    },

    render() {
        let orderIdView = null;
        orderIdView = <a type="primary" onClick={() => this.setModalVisible(true)}>点击</a>;
        return (
            <div>
                {orderIdView}
                <Modal
                    title="显示model"
                    visible={this.state.modalVisible}
                    onOk={this.handleSubmit}
                    onCancel={() => {
                        this.setModalVisible(false);
                        this.props.form.resetFields();
                    }}
                    footer={[
                        <Button key="submit" type="primary" size="large" loading={this.state.loading} onClick={this.handleSubmit}>确定</Button>,
                        <Button key="back" size="large" onClick={() => {
                            this.setModalVisible(false);
                            this.props.form.resetFields();
                        }}>取消</Button>,
                    ]}
                >
                    <div>
                        <div style={{ borderBottom: '1px solid #E9E9E9' }}>
                            <Checkbox
                                indeterminate={this.state.indeterminate}
                                onChange={this.onCheckAllChange}
                                checked={this.state.checkAll}
                            >
                                Check all
                            </Checkbox>
                        </div>
                        <br />
                        <CheckboxGroup indeterminate={this.state.indeterminate} options={plainOptions} value={this.state.checkedList} onChange={this.onChange} />
                    </div>
                </Modal>
            </div>
        );
    }
});

const ActiveConfigForm = Form.create()(ActiveConfigModal);
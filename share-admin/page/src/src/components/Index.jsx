import React,{Component} from 'react';
import connectToStores from 'alt-utils/lib/connectToStores';
import IndexStore from 'stores/IndexStore.js';
import IndexAction  from 'actions/IndexAction.js';
import Layout from 'components/common/Layout';
import {Button,Table, Modal, Checkbox, Form, Row, Col, Input, Switch, Icon, Upload, Transfer} from 'antd';
import styles from './indexPage.less';
import {getYMDW} from 'services/functions.js';

const CheckboxGroup = Checkbox.Group;

const plainOptions = ['Apple', 'Pear', 'Orange'];
const defaultCheckedList = ['Apple', 'Pear', 'Orange'];

const columns = [{
  title: '姓名',
  dataIndex: 'name',
  key: 'name',
    render(text, record) {
      return (
          <div>
              <div>{record.name}</div>
          </div>
        );
    }
}, {
  title: '年龄',
  dataIndex: 'age',
  key: 'age',
    render(text, record) {
        return (
            <div>
                <div>{record.age}</div>
            </div>
        );
    }
}, {
  title: '地址',
  dataIndex: 'address',
  key: 'address',
    render(text, record) {
        return (
            <div>
                <div>{record.address}</div>
            </div>
        );
    }
}, {
    title: 'Action',
    dataIndex: 'action',
    key: 'action',
    render(text, record) {
        return (
            <div>
                <ActiveConfigForm/>
            </div>
        );
    }
}];

const weekDays = [{
    title: '星期一',
    dataIndex: 'monday',
    key: 'monday',
    render(text, record) {
        return (
            <div>
                <MenuInfo/>
            </div>
        );
    }
}, {
    title: '星期二',
    dataIndex: 'tuesday',
    key: 'tuesday',
    render(text, record) {
        return (
            <div>
                <MenuInfo/>
            </div>
        );
    }
}, {
    title: '星期三',
    dataIndex: 'wednesday',
    key: 'wednesday',
    render(text, record) {
        return (
            <div>
                <MenuInfo/>
            </div>
        );
    }
}, {
    title: '星期四',
    dataIndex: 'thursday',
    key: 'thursday',
    render(text, record) {
        return (
            <div>
                <MenuInfo/>
            </div>
        );
    }
}, {
    title: '星期五',
    dataIndex: 'friday',
    key: 'friday',
    render(text, record) {
        return (
            <div>
                <MenuInfo/>
            </div>
        );
    }
}];

class Index extends Component {

	static getStores(){
		return [IndexStore]
	}

	static getPropsFromStores(){
		let state = IndexStore.getState();
		console.log('Global State :',state)
		return {
            dataList: state.dataList
		}
	}

    componentDidMount() {
	    IndexAction.getMoreData();
    }

	render(){
		return(
        <div>
            <Layout>
                <Row className={styles.antRow}>
                    <Col span={12}> {getYMDW(new Date())} 尊敬的伍胜胜！<Switch checkedChildren="已开启自动点餐" unCheckedChildren="未开启自动点餐" /></Col>
                    <Col span={12}>
                        <Row>
                            <Col span={17}><Input style={{width:'350px'}} placeholder="登录美餐网获取的token"/></Col>
                            <Col span={6}><Button type="primary" icon="search" >获取点餐信息</Button></Col>
                        </Row>
                    </Col>
                </Row>
                <Row className={styles.antRow}>
                    <Col span={12}><a>今天已点</a>佛跳墙</Col>
                    <Col span={12}><a>发送到QQ</a></Col>
                </Row>
                <Table columns={weekDays} dataSource={['hello']} bordered pagination={false}/>
                {/*<Table columns={columns} dataSource={this.props.dataList} bordered/>*/}
                <SelectMenu/>
            </Layout>
        </div>

		)
	}
}

export default connectToStores(Index);

export class MenuInfo extends Component {

    render () {
        return (
            <div>
                <Row>
                    <Col span={12}><PicturesWall/></Col>
                    <Col span={12} style={{padding:'10px'}}>
                        <p>{ '水果沙拉' }</p>
                        <p>店铺：{ }</p>
                        <a>更换菜谱</a>
                    </Col>
                </Row>
            </div>
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
            url: 'https://zos.alipayobjects.com/rmsportal/jkjgkEfvpUPVyRjUImniVslZfWPnJuuZ.png',
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

export class SelectMenu extends Component {
    state = {
        mockData: [],
        targetKeys: [],
    }
    componentDidMount() {
        this.getMock();
    }
    getMock = () => {
        const targetKeys = [];
        const mockData = [];
        for (let i = 0; i < 20; i++) {
            const data = {
                key: i.toString(),
                title: `content${i + 1}`,
                description: `description of content${i + 1}`,
                chosen: Math.random() * 2 > 1,
            };
            if (data.chosen) {
                targetKeys.push(data.key);
            }
            mockData.push(data);
        }
        this.setState({ mockData, targetKeys });
    }
    handleChange = (targetKeys) => {
        this.setState({ targetKeys });
    }
    renderFooter = () => {
        return (
            <Button
                size="small"
                style={{ float: 'right', margin: 5 }}
                onClick={this.getMock}
            >
                刷新
            </Button>
        );
    }
    render() {
        return (
            <div style={{marginTop: '10px'}}>
                <p style={{fontSize:'12pt', marginBottom: '10px'}}>批量选择菜单</p>
                <Transfer
                    dataSource={this.state.mockData}
                    showSearch
                    listStyle={{
                        width: 250,
                        height: 300,
                    }}
                    className = {styles.menusCard}
                    operations={['选中', '去除']}
                    targetKeys={this.state.targetKeys}
                    onChange={this.handleChange}
                    render={item =>`${item.title}-${item.description}`}
                    footer={this.renderFooter}
                />
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
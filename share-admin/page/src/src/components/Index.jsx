import React,{Component} from 'react';
import connectToStores from 'alt-utils/lib/connectToStores';
import IndexStore from 'stores/IndexStore.js';
import IndexAction  from 'actions/IndexAction.js';
import Layout from 'components/common/Layout';
import {Button,Table, Modal, Input, Checkbox, Select,
    Form, InputNumber, Switch, Radio,
    Slider, Upload, Icon} from 'antd';

const CheckboxGroup = Checkbox.Group;
const FormItem = Form.Item;
const Option = Select.Option;
const RadioButton = Radio.Button;
const RadioGroup = Radio.Group;

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
                <ActiveConfigModal/>
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
                <br/>
                <Table columns={columns} dataSource={this.props.dataList} bordered/>
            </Layout>
        </div>

		)
	}
}

export default connectToStores(Index);

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
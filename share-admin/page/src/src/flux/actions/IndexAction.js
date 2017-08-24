import Alt from 'bases/Alt.js';

class IndexAction{
    initTest(){
        console.log("init my test");
        return 'hello';
    }

  getMoreData(){
    return null;
  }
}

export default Alt.createActions(IndexAction);

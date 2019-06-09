// pages/notes/notes.js
const app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    noteList:[]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    
  },

  onShow:function(){
    this.setData({
      noteList: app.globalData.noteList
    })
  },
  searchWord:function(e){
    var word = e.currentTarget.dataset.word
    app.globalData.word = word
    wx.switchTab({
      url: '../index/index',
    })
  }

})
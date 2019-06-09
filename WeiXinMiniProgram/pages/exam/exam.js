// pages/exam/exam.js
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    level:-1,
    levels:[0,1,2,3,4,5],
    wordTrans:[],
    random:-1,
    selected:-1,
    complete:false,
    right:0,
    finished:0,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    
  },

  selectLevel:function(e){
    this.setData({
      level : e.currentTarget.dataset.level
    })
    this.fresh();
  },

  fresh:function(){
    
    var that = this
    this.setData({
      random:-1,
      selected:-1,
      complete:false,
    })
    wx.request({
      url: 'http://localhost:8080/demo?function=random&levels='+this.data.level,
      headers: {
        'Content-Type': 'application/json'
      },
      success: function (res) {
        console.log("Get random words successfully.")
        var words = res.data
        var random = Math.round(Math.random() * (words.length - 1))
        console.log(words)
        that.setData({
          wordTrans: words,
          random: random,
        })
      }
    })
  },
  selectAnswer:function(e){
    var answer = e.currentTarget.dataset.ans
    var right = this.data.right
    var finished = this.data.finished + 1
    if (answer == this.data.random) {
      right = right + 1
    } else {
      console.log("wrong")
    }
    this.setData({
      selected:answer,
      complete:true,
      right:right,
      finished:finished
    })
  },
  reset:function(){
    this.setData({
      level: -1,
      wordTrans: {},
      random: -1,
      selected: -1,
      complete: false,
      right: 0,
      finished: 0,
    })
  },
  search:function(){
    app.globalData.word = this.data.wordTrans[this.data.random].word
    wx.switchTab({
      url: '../index/index',
    })
  }
})
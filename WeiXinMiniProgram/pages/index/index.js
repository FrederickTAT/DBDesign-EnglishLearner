//index.js
//获取应用实例
const app = getApp()

Page({
  data: {
    word:"",
    wordInfo:{},
    added:false,
  },
  //事件处理函数
  onLoad: function () {
    this.getNotes()
  },
  onShow:function(){
    this.setData({
      word:app.globalData.word,
    })
    if(this.data.word!="" && (!this.data.wordInfo || (this.data.wordInfo && this.data.word!=this.data.wordInfo.word))){
      this.searchWord(this.data.word)
    }
  },
  searchInput:function(e){
    this.setData({
      word:e.detail.value
    })
  },
  searchWord:function(){
    var that = this
    var word = that.data.word
    app.globalData.word = word
    if (word == "") {
      return
    }
    wx.request({
      url: 'http://localhost:8080/demo?function=search&word=' + word,
      headers: {
        'Content-Type': 'application/json'
      },
      success: function (res) {
        console.log("Get wordInfo successfully.")
        that.setData({
          wordInfo:res.data
        })
        that.modifyNoteButtonStatus()
      }
    })
    
  },
  getNotes: function () {
    var that = this
    wx.request({
      url: 'http://localhost:8080/demo?function=note&item=get',
      headers: {
        'Content-Type': 'application/json'
      },
      success: function (res) {
        console.log("Get notes successfully.")
        app.globalData.noteList = res.data
        that.modifyNoteButtonStatus()
      }
    })
  },
  addNote:function(){
    var word = this.data.word
    var that = this
    wx.request({
      url: 'http://localhost:8080/demo?function=note&item=add&word='+word,
      headers: {
        'Content-Type': 'application/json'
      },
      success: function (res) {
        console.log("Add notes successfully.")
        that.getNotes()
      }
    })
  },
  deleteNote: function () {
    var word = this.data.word
    var that = this
    wx.request({
      url: 'http://localhost:8080/demo?function=note&item=delete&word=' + word,
      headers: {
        'Content-Type': 'application/json'
      },
      success: function (res) {
        console.log("Delete notes successfully.")
        that.getNotes()
      }
    })
  },
  modifyNoteButtonStatus:function(){
    var word = app.globalData.word
    this.setData({
      added: false
    })
    for (var index in app.globalData.noteList) {
      var note = app.globalData.noteList[index]
      if (word == note.word) {
        this.setData({
          added: true
        })
        break
      }
    }
  }
})

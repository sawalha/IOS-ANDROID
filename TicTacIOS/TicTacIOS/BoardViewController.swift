//
//  BoardViewController.swift
//  TicTacIOS
//
//  Created by mohammad sawalha on 7/9/15.
//  Copyright (c) 2015 mohammad sawalha. All rights reserved.
//

import UIKit

class BoardViewController: UIViewController,UIAlertViewDelegate {
    
    var boardView:UIView!;
    let x_icon = UIImage(named: "x")
    let o_icon = UIImage(named: "oo")
    var imageViews = [UIImageView]()
    var boardArray:[Int] = []
    var My_Turn :Bool = true;
    enum GameType{
        case OneVsOne;
        case Easy;
        case Normal;
    }
    var gameType:GameType?
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "bg.jpg")!)
        creatBoard()
    }
    
    func creatBoard(){
        My_Turn=true
        imageViews = [UIImageView]()
        boardArray = [Int] (count: 9, repeatedValue: 0)
        boardView = UIView(frame: CGRect(x: 0, y: 0, width: 230, height: 230))
        boardView.center = view.center
        var x = 0
        var y = 0
        for i in 1...9{
            let imageView = UIImageView(frame: CGRect(x: x, y: y, width: 70, height: 70))
            x += 80
            if(i%3 == 0){
                x = 0
                y += 80
            }
            imageView.userInteractionEnabled = true
            imageView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: "play:"))
            imageView.tag = i-1
            imageView.backgroundColor = UIColor.lightGrayColor()
            imageViews.append(imageView)
            boardView.addSubview(imageView)
        }
        view.addSubview(boardView)
        
        if (gameType == GameType.OneVsOne){
            println("OneVsOne")
        }
        if (gameType == GameType.Easy){
            println("easy")
        }
        if (gameType == GameType.Normal){
            println("normal")
        }
        
    }
    func play(sender: UIGestureRecognizer){
        let cellID = sender.view!.tag
        println("tapped \(cellID)");
        if(boardArray[cellID] != 0){
            let alert:UIAlertView = UIAlertView(title: "InValid", message: "not empty", delegate: self, cancelButtonTitle: "OK")
            alert.show()
        }else{
        imageViews[cellID].image = My_Turn ? x_icon : o_icon
        boardArray[cellID] = My_Turn ? 1 : 2
        
        if( gameType == GameType.OneVsOne){
            My_Turn = !My_Turn
        }else {
            if(My_Turn){
                PlayVsMobile()
            }
        }
        
        checkForWinner()
        
        }}
    func alertView(alertView: UIAlertView, clickedButtonAtIndex buttonIndex: Int){
        if(alertView.tag == 1){
            if (buttonIndex == 0){
                println("no")
                /* let finish = self.storyboard?.instantiateViewControllerWithIdentifier("ViewController") as! ViewController
                self.presentViewController(finish, animated: true, completion: nil)*/
                self.dismissViewControllerAnimated(true, completion: nil)
            }else {
                println("yes")
                creatBoard()
            }
            
        }
    }
    
    
    func checkForWinner(){
        let alert:UIAlertView = UIAlertView(title: "", message: "Do you want to play again?", delegate: self, cancelButtonTitle: "No", otherButtonTitles: "Yes")
        alert.tag = 1
        var tag:Int = 0
        for i in 0...2{
            if(boardArray[i] != 0 && boardArray[i] == boardArray[i+3] && boardArray[i] == boardArray[i+6]){
                tag = boardArray[i] == 1 ? 1 : 2
            }
            var row = i * 3;
            if(boardArray[row] != 0 && boardArray[row] == boardArray[row + 1] && boardArray[row] == boardArray[row + 2]){
                tag = boardArray[row] == 1 ? 1 : 2
            }
        }
        if (boardArray[0] != 0 && boardArray[0] == boardArray[4] && boardArray[0] == boardArray[8]){
            tag = boardArray[4] == 1 ? 1 : 2
        }else {
            if (boardArray[2] != 0 && boardArray[2] == boardArray[4] && boardArray[2] == boardArray[6]){
                tag = boardArray[4] == 1 ? 1 : 2
            }
        }
        if(tag != 0){
            
            if(gameType != GameType.OneVsOne){
                alert.title = tag == 1 ? "you win" : "oops you lost"
            }else {
                alert.title = tag == 1 ? "X win" : "O win"
            }
            alert.show()
        }
        else{
            tag = 3
            for i in 0...8 {
                if(boardArray[i] == 0){
                    tag = 0
                    break
                }
            }
        }
        if(tag == 3 ){
            alert.title = "Draw"
            alert.show()
        }
    }
    
    func MobilePlay(cell:Int){
        imageViews[cell].image = o_icon
        boardArray[cell] = 2
    }
    
    func PlayVsMobile(){
        if(gameType == GameType.Normal && boardArray[4] == 0){
            MobilePlay(4)
            return
        }
        for i in 0...2{
            var row = i * 3;
            if(boardArray[row] != 0 && boardArray[row] == boardArray[row + 1] && boardArray[row+2] == 0){
                MobilePlay(row+2)
                return
            }
            if(boardArray[row] != 0 && boardArray[row] == boardArray[row + 2] && boardArray[row+1] == 0){
                MobilePlay(row+1)
                return
            }
            if(boardArray[row+1] != 0 && boardArray[row+2] == boardArray[row + 1] && boardArray[row] == 0){
                MobilePlay(row)
                return
            }
            
            if(boardArray[i] != 0 && boardArray[i] == boardArray[i+3] && boardArray[i+6] == 0){
                MobilePlay(i+6)
                return
            }
            if(boardArray[i] != 0 && boardArray[i] == boardArray[i+6] && boardArray[i+3] == 0){
                MobilePlay(i+3)
                return
            }
            if(boardArray[i+3] != 0 && boardArray[i+6] == boardArray[i+3] && boardArray[i] == 0){
                MobilePlay(i)
                return
            }
        }
        if(boardArray[0] != 0 && boardArray[0] == boardArray[4] && boardArray[8] == 0){
            MobilePlay(8)
            return
        }
        if(boardArray[8] != 0 && boardArray[8] == boardArray[4] && boardArray[0] == 0){
            MobilePlay(0)
            return
        }
        if(boardArray[2] != 0 && boardArray[2] == boardArray[4] && boardArray[6] == 0){
            MobilePlay(6)
            return
        }
        if(boardArray[6] != 0 && boardArray[6] == boardArray[4] && boardArray[2] == 0){
            MobilePlay(2)
            return
        }
        if(boardArray[4] == 0){
            MobilePlay(4)
            return
        }
        if(gameType == GameType.Normal){
            for i in 0...8
            {
                if(i != 1 && i != 4 && i != 7){
                    if(boardArray[i]==0){
                        MobilePlay(i)
                        return
                    }
                }
            }
        }
        
        for i in 0...8
        {
            if(boardArray[i]==0){
                MobilePlay(i)
                return
            }
        }
        
    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
}


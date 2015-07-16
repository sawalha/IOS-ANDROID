//
//  Board.swift
//  TicTacIOS
//
//  Created by mohammad sawalha on 7/12/15.
//  Copyright (c) 2015 mohammad sawalha. All rights reserved.
//

import Foundation


enum Status{
    case X;
    case O;
    case Clear;
}

enum MoveResult{
    case ValidMove;
    case InvalidMove;
    case Victory;
    case Draw;
}


class Board {
    private var gameBoard:[Status]!;
    private var isX:Bool!;
    init(){
        gameBoard = [Status]();
        isX = true;
        for i in 1...9{
            gameBoard.append(Status.Clear);
        }
    }
    
    func move(cell:Int)->MoveResult{
        if(cell<1 || cell>9){
            return MoveResult.InvalidMove;
        }
        if(gameBoard[cell-1] == Status.Clear){
            gameBoard[cell-1] = isX! ? Status.X : Status.O;
            isX = !isX;
            if checkForVictory(){
                return MoveResult.Victory;
            }else{
                return MoveResult.ValidMove;
            }
            
            
        }
        return MoveResult.InvalidMove;
    }
    
    func checkForVictory()->Bool{
        for i in 0...2{
            if(gameBoard[i] != Status.Clear && gameBoard[i] == gameBoard[i+3] && gameBoard[i] == gameBoard[i+6]){
                return true;
            }
            var row = i * 3;
            if(gameBoard[row] != Status.Clear && gameBoard[row] == gameBoard[row + 1] && gameBoard[row] == gameBoard[row + 2]){
                return true;
            }
        }
        var diagonal1 = gameBoard[0] != Status.Clear && gameBoard[0] == gameBoard[4] && gameBoard[0] == gameBoard[8];
        
        var diagonal2 = gameBoard[2] != Status.Clear && gameBoard[2] == gameBoard[4] && gameBoard[2] == gameBoard[6];
        
        if (diagonal1 || diagonal2){
            return true;
        }
        
        return false;
    }
    
    // _ _ _ _ _ _ _ _ _
    // 0 1 2 3 4 5 6 7 8
    
    
    // 0 1 2
    // 3 4 5
    // 6 7 8
    
    
    
    
    func isXturn()->Bool{
        return isX!;
    }
    
}

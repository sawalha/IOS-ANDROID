//
//  ViewController.swift
//  TicTacIOS
//
//  Created by mohammad sawalha on 7/7/15.
//  Copyright (c) 2015 mohammad sawalha. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    

    @IBOutlet var image: UIImageView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "bg.jpg")!)
      
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func Online(sender: UIButton) {
        let alert:UIAlertView = UIAlertView(title: "Comming soon", message: "Comming soon", delegate: self, cancelButtonTitle: "OK")
        alert.show()
    }
    @IBAction func OneVsOne(sender: UIButton) {
        let start = self.storyboard?.instantiateViewControllerWithIdentifier("BoardViewController") as! BoardViewController
            self.presentViewController(start, animated: true, completion: nil)
        start.gameType = BoardViewController.GameType.OneVsOne

    }
    
    @IBAction func Easy(sender: UIButton) {
             let start = self.storyboard?.instantiateViewControllerWithIdentifier("BoardViewController") as! BoardViewController
            self.presentViewController(start, animated: true, completion: nil)
           start.gameType = BoardViewController.GameType.Easy
    }

    @IBAction func Normal(sender: UIButton) {
        let start = self.storyboard?.instantiateViewControllerWithIdentifier("BoardViewController") as! BoardViewController
            self.presentViewController(start, animated: true, completion: nil)
        start.gameType = BoardViewController.GameType.Normal
    }
}


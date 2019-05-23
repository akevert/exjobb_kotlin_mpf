//
//  RepoTableViewCell.swift
//  MPFiOS
//
//  Created by Anna-Karin Evert on 2019-05-04.
//  Copyright © 2019 Anna-Karin Evert. All rights reserved.
//

import UIKit

class RepoTableViewCell: UITableViewCell {
    
    @IBOutlet weak var repoName: UILabel!
    @IBOutlet weak var nrStars: UILabel!
    @IBOutlet weak var nrForks: UILabel!
    @IBOutlet weak var nrWatchers: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
}

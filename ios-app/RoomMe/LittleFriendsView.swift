//
//  LittleFriendsView.swift
//  RoomMe
//
//  Created by Student2 on 18/05/2022.
//

import SwiftUI

struct Task3: Identifiable{
    var id = UUID()
    var description: String
}

struct TaskRow3: View {
    var task: Task3
    var body: some View {
        HStack{
        Image(systemName: "person.circle")
            .foregroundColor(.gray)
            .imageScale(.large)
        Text("\(task.description)")
        }
    }
}

var first3 = Task3(description: "smerf gapcio")


struct LittleFriendsView: View {
    @Binding var sceneNumber: Int
    @State var tasks = [first3]
    var body: some View {
        VStack{
            HStack {
            List(tasks) {
                task in TaskRow3(task: task)
            }}
        NavigationView {
            //Spacer()
            
            VStack{
                Spacer()
                HStack {
                    
                    
                    NavigationLink(destination: NewRoommateView(tasks: $tasks), label: {
                Text("ADD NEW FRIEND")
                    .padding()
                    .background(Color.green)
                     .foregroundColor(Color.white)
                     .cornerRadius(10)
            }
            )
                }
            }
            }
        }
    }
}



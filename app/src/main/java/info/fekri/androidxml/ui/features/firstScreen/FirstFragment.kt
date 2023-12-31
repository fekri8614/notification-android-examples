package info.fekri.androidxml.ui.features.firstScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import info.fekri.androidxml.databinding.FragmentFirstBinding
import info.fekri.androidxml.model.data.TvMazeItem
import info.fekri.androidxml.model.net.ApiManager
import io.reactivex.disposables.CompositeDisposable

class FirstFragment : Fragment(), FirstRecyclerAdapter.PersonEvent,
    ApiManager.ServerEvent<List<TvMazeItem>> {

    private lateinit var binding: FragmentFirstBinding
    private val apiManager: ApiManager = ApiManager()
    private val compositeDisposable = CompositeDisposable()
    private lateinit var mAdapter: FirstRecyclerAdapter

    companion object {
        private const val TAG = "FirstFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiManager.getPersons(this, compositeDisposable)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun setupData(data: ArrayList<TvMazeItem>) {
        mAdapter = FirstRecyclerAdapter(data, this)
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        binding.recyclerMainFirstScreen.adapter = mAdapter
        binding.recyclerMainFirstScreen.layoutManager = layoutManager
    }

    override fun onError(msg: String) {
        Log.e(TAG, msg)
        binding.apply {
            recyclerMainFirstScreen.visibility = View.GONE

            txtFirstShowError.visibility = View.VISIBLE
            txtFirstShowError.text = msg
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(data: List<TvMazeItem>) {
        setupData(ArrayList(data))
    }

    override fun onPersonClicked(personItem: TvMazeItem, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onPersonLongClicked(personItem: TvMazeItem, position: Int) {
        // remove item when long clicked:
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("Remove Item")
        dialog.setMessage("Do you really want to remove this item?")

        dialog.setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()

            mAdapter.deleteItem(personItem, position)
        }
        dialog.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
    }

}